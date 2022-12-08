package com.van.mngr.integration.server.websocket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class TestController {

    @GetMapping("count-sessions")
    public Mono<String> getSessionCount() {
        return Mono.just("Session count: " + RouterConfiguration.sessions.size());
    }
}
