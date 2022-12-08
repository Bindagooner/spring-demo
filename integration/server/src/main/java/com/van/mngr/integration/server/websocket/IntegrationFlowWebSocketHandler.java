package com.van.mngr.integration.server.websocket;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import com.van.mngr.integration.server.data.DataService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class IntegrationFlowWebSocketHandler implements WebSocketHandler {

    @Autowired
    private IntegrationFlowContext integrationFlowContext;

    private Gson gson = new Gson();

    @Autowired
    private DataService dataService;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        final String sessionId = session.getId();

        // handle message received.
        AtomicReference<String> symbol = new AtomicReference<>();
        session.receive().subscribe(webSocketMessage -> {
            Map<String, String> map = gson.fromJson(webSocketMessage.getPayloadAsText(), Map.class);
            symbol.set(map.get("symbol"));
        });

        Flux<Message<?>> input = dataService.getMessages().map(m -> MessageBuilder.withPayload(m).build());

        Publisher<Message<WebSocketMessage>> messagePublisher =
                IntegrationFlows.from(input)
//                        .<Map>filter(p -> symbol.get().equals(String.valueOf(p.get("symbol"))))
                        .<Map>handle((p,h) -> {
                            String fmtTime = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT).format(new Date());
                            p.put("time", fmtTime);
                            return p;
                        })
                        .<Map, Message<WebSocketMessage>>transform(p -> {
                            log.debug("transform to WebSocketMessage: {}", p);
                            return MessageBuilder.withPayload(session.textMessage(gson.toJson(p))).build();
                        })
//                        .log(LoggingHandler.Level.INFO)
                        .toReactivePublisher();

        Flux<WebSocketMessage> output = Flux.from(messagePublisher).map(Message::getPayload);

        IntegrationFlowContext.IntegrationFlowRegistration flowRegistration =
                this.integrationFlowContext.registration((IntegrationFlow) messagePublisher).register();

        if (RouterConfiguration.sessions.add(sessionId)) {
            log.info("Starting WebSocket session: {}", sessionId);
            WebSocketMessage msg = session.textMessage(String.format("{\"session\":\"%s\"}", sessionId));
            // Register the outbound flux as the source of outbound messages
            final Flux<WebSocketMessage> outFlux = Flux.concat(Flux.just(msg), output.map(metric -> {
                log.info("Sending message to client [{}]: {}", sessionId, metric.getPayloadAsText());
                return session.textMessage(metric.getPayloadAsText());
            }));
            // Subscribe to the inbound message flux
            session.receive().doFinally(sig -> {
                log.info("Terminating WebSocket Session (client side) sig: [{}], [{}]", sig.name(), sessionId);
                session.close();
                RouterConfiguration.sessions.remove(sessionId);  // remove the stored session id
            });
            return session.send(outFlux);
        }

        return Mono.empty();
    }
}
