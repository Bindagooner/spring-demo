package neospider.mngr.integration.server.websocket;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CommonController {

    @GetMapping("count-sessions")
    public Mono<String> getSessionCount() {
        return Mono.just("Session count: " + SessionManager.sessions.size());
    }

    @DeleteMapping("remove-session/{session}")
    public Mono<Void> deleteSession(@PathVariable String session) {
        SessionManager.sessions.remove(session);
        return Mono.empty();
    }
}
