package neospider.mngr.integration.server;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MessageHandler {

    private static AtomicInteger count = new AtomicInteger();

    // handle message
    public static Flux<Map> upperCaseValue(Map<String, String> payload, Map<String, Object> headers) {
        log.info("payload: {}", payload);

        return Flux.fromStream(
                payload.entrySet().parallelStream()
                        .map(entry -> Collections.singletonMap(entry.getKey(),
                                entry.getValue().toUpperCase() + "-" + count.incrementAndGet()))
        );
    }
}
