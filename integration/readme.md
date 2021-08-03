# Spring integration demo

## HTTP async with Netty + Webflux
send, receive message in Map
### Server
- Spring integration + Webflux reactor
```java
class IntegrationConfiguration {
    @Bean
    public IntegrationFlow sseFlow() {
        return IntegrationFlows
                .from(WebFlux.inboundGateway("/sse")
                        .requestMapping(m -> {
                            m.produces(MediaType.TEXT_EVENT_STREAM_VALUE);
                            m.methods(HttpMethod.POST);
                        }))
                .convert(Map.class)
                .handle(MessageHandler::upperCaseValue)
                .get();
    }
}
```

### Client

- use Webflux WebClient to send/receive message.
```java
class DemoSendReceive {
    WebClient client = WebClient.create("http://localhost:8080");
    void execute() {
        client.post().uri("/sse")
                .body(Mono.just(request), Map.class)
                .retrieve()
                .bodyToFlux(Map.class)
                .subscribe(MessageReceiver::handle);
    }
}
```

### How to run:
- cd to the folder server then run command:
> mvn spring-boot:run

- cd to the folder client then run command:
> mvn spring-boot:run

- In this demo, I am trying to upper case the value sent from client. The request and response are in Map<String, Object>.
- Client send message to server every 10 seconds multi-threaded.
- Log in server log file when message received: 
> 2021-06-23 18:53:50.007  INFO 38867 --- [ctor-http-nio-3] n.m.integration.server.MessageHandler    : payload: {key-1=zvqvpwjqjw, key-0=gcbmlmihsd, key-5=ldgqdnfbaa, key-4=ymzfywhxuc, key-3=vitbzvdnur, key-2=kvwuwowosy, key-9=yxnjgfbmhi, key-8=aolsjxlbyb, key-7=zmcnyhysxj, key-6=gdrtdelggw}
- Log in client log file when response received:
>2021-06-23 18:53:40.032  INFO 38925 --- [ctor-http-nio-2] n.m.integration.client.MessageReceiver   : data received: {key-5=JXYNINVNCT-540}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-3] n.m.integration.client.MessageReceiver   : data received: {key-9=TUAZQBAHPF-568}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-5] n.m.integration.client.MessageReceiver   : data received: {key-8=SXTBBCAAQG-554}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-1] n.m.integration.client.MessageReceiver   : data received: {key-1=YCPSTYNXNO-527}
 2021-06-23 18:53:40.032  INFO 38925 --- [ctor-http-nio-6] n.m.integration.client.MessageReceiver   : data received: {key-4=LJYJVMKRVK-525}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-8] n.m.integration.client.MessageReceiver   : data received: {key-8=JTERZIUJRG-538}
 2021-06-23 18:53:40.033  INFO 38925 --- [tor-http-nio-10] n.m.integration.client.MessageReceiver   : data received: {key-2=QUVDLYETMN-461}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-3] n.m.integration.client.MessageReceiver   : data received: {key-8=DNJMIKGAGF-573}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-5] n.m.integration.client.MessageReceiver   : data received: {key-7=EVLNZIVXLG-560}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-2] n.m.integration.client.MessageReceiver   : data received: {key-4=PKOCJHAYAM-546}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-1] n.m.integration.client.MessageReceiver   : data received: {key-0=UPFYMNNBOS-531}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-6] n.m.integration.client.MessageReceiver   : data received: {key-3=HSJOJTUOMU-574}
 2021-06-23 18:53:40.033  INFO 38925 --- [tor-http-nio-12] n.m.integration.client.MessageReceiver   : data received: {key-3=JYMFWUKZUN-553}
 2021-06-23 18:53:40.033  INFO 38925 --- [ctor-http-nio-3] n.m.integration.client.MessageReceiver   : data received: {key-7=PPRDGBTKLB-579}

## Spring reactive WebSocket and Spring integration

### Maven dependencies:
```xml
<dependencies>
 ...
 <dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-webflux</artifactId>
 </dependency>
 <dependency>
  <groupId>org.springframework.integration</groupId>
  <artifactId>spring-integration-webflux</artifactId>
 </dependency>
 <dependency>
  <groupId>org.springframework.integration</groupId>
  <artifactId>spring-integration-websocket</artifactId>
 </dependency>
 ...
</dependencies>
```

### Implementation

To make server realize incoming request, add route to specific handler. Below is how to add router for index.html and websocket endpoint:

```java
class RouterConfiguration{
    @Bean
    public RouterFunction<ServerResponse> htmlRouter(@Value("classpath:/static/index.html") Resource html) {
     return route(
             GET("/"),
             request -> ok()
                     .contentType(MediaType.TEXT_HTML)
                     .syncBody(html)
            ).andRoute(GET("index.html"),
             request -> ok()
                     .contentType(MediaType.TEXT_HTML)
                     .syncBody(html));
    }
   
    @Bean
    public HandlerMapping handlerMapping(IntegrationFlowWebSocketHandler integrationFlowWebSocketHandler) {
     Map<String, WebSocketHandler> map = new HashMap<>();
     map.put("/time/**", integrationFlowWebSocketHandler);
     map.put("/time**", integrationFlowWebSocketHandler);
   
     SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
     mapping.setUrlMap(map);
     mapping.setOrder(-1); // before annotated controllers
     return mapping;
    }
}

```

Notice class IntegrationFlowWebSocketHandler. This is implemented WebSocketHandler. In this demo, IntegrationFlow is defined here. 
This class handle websocket request/response as well.

```java
public class IntegrationFlowWebSocketHandler implements WebSocketHandler {
   @Override
   public Mono<Void> handle(WebSocketSession session) {
    session.receive().subscribe(webSocketMessage -> {
     // handle request's payload 
    });
    
    
    
    // outFlux is outbound websocket channel, streaming data
    return session.send(outFlux);
   }
   
}
```
Integration flow
```java
    // input can be Flux, MessageSource, Inbound message channel
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
                    .toReactivePublisher();
    
    // Flux output is used to publish data to client 
    Flux<WebSocketMessage> output = Flux.from(messagePublisher).map(Message::getPayload);

    IntegrationFlowContext.IntegrationFlowRegistration flowRegistration =
        this.integrationFlowContext.registration((IntegrationFlow) messagePublisher).register();
    
```

### How to run
for netty embedded, run command:
> mvn spring-boot:run

for container netty:
> TBD

Open browser:
> http://localhost:8080/index.html

Script file named "auto-ws.js" is used for load test, can run in browser or node.

### Link refer:
understand websocket request/response handle
> https://blog.devgenius.io/websockets-with-spring-webflux-ba9d0b47b348

Spring Websocket + webflux + integration.
https://stackoverflow.com/questions/52291206/spring-integration-and-reactive-websockets

github repo from Spring Integration author
> https://github.com/artembilan/sandbox/tree/master/spring-integration-websocket-reactive/