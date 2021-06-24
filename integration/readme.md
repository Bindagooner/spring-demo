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

