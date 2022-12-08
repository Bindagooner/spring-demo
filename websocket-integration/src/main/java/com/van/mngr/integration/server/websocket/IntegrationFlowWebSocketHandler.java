package com.van.mngr.integration.server.websocket;

import com.google.gson.Gson;
import com.van.mngr.integration.server.data.DataService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

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

        CopyOnWriteArraySet<String> symbols = new CopyOnWriteArraySet<>();

        // build a Flux of Integration message from flux from data service.
        Flux<Message<?>> input = dataService.getMessages()
                .map(m -> MessageBuilder.withPayload(m).build()); // map a Map to Message object.

        if (SessionManager.sessions.add(sessionId)) {
            log.info("Starting WebSocket session: {}", sessionId);

            // We have 2 way to build integration. this is called integration dsl.
            // based on java 8 functional programing, lambda, method reference.
            Publisher<Message<WebSocketMessage>> messagePublisher =
                    IntegrationFlows.from(input)
                            // Populate a MessageFilter. input type is message type at this time. return boolean.
                            // similar to functional interface Predicate.
                            // here trying to filter symbol expected by client.
                            .<Map>filter(p -> symbols.isEmpty() || symbols.contains(String.valueOf(p.get("symbol"))))
                            // populate a ServiceActivatingHandler. Input is payload of message and message header.
                            // Similar functional interface BiFunction
                            // Basically this is used to handle message.
                            // here trying to put time to the map payload.
                            .<Map>handle((p, h) -> {
                                String fmtTime = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT).format(new Date());
                                p.put("time", fmtTime);
                                return p;
                            })
                            // populate a message transform handler. input is current type of message. output is expected type of message.
                            // here trying to build Message WebSocketMessage from Map.
                            .<Map, Message<WebSocketMessage>>transform(p -> {
                                log.debug("transform to WebSocketMessage: {}", p);
                                return MessageBuilder.withPayload(session.textMessage(gson.toJson(p))).build();
                            })
                            // push message to a channel, here is queue.
                            // I faced error message 'integrationflow reactive Dispatcher has no subscribers' when a client disconnected. Program is still working.
                            // Place queue here then that error message gone. Not sure about this one.
                            .channel(queueChannel())
                            // build a publisher, which publish message to subscribers.
                            .toReactivePublisher();

            // map above Flux to WebSocketMessage Flux
            Flux<WebSocketMessage> output = Flux.from(messagePublisher).map(Message::getPayload);

            IntegrationFlowContext.IntegrationFlowRegistration flowRegistration =
                    this.integrationFlowContext.registration((IntegrationFlow) messagePublisher).register();

            // Subscribe to the inbound message flux
            session.receive()
                    .doFinally(sig -> {
                        log.info("Terminating WebSocket Session (client side) sig: [{}], [{}]", sig.name(), sessionId);
                        session.close();
                        SessionManager.sessions.remove(sessionId); // remove the stored session id
                        flowRegistration.destroy();
                    })
                    .doOnError(error -> log.error("SessionId : {} got error: {}", session.getId(), error.getMessage())
                    ).subscribe(webSocketMessage -> {
                        // handle message received.
                        Map<String, String> map = gson.fromJson(webSocketMessage.getPayloadAsText(), Map.class);
                        symbols.add(map.get("symbol"));
                        log.info("Received request for stock symbol [{}]. Session Id: [{}]", map.get("symbol"), session.getId());
            });

            return session.send(output);
        }

        return Mono.empty();
    }

    // this is one of ways to build Spring integration flow. deal with beans, annotations.
    // like Channel, Message handler, Message transformer, Poller, Service Activator.
    @Bean
    MessageChannel queueChannel() {
        return new QueueChannel(100);
    }
}
