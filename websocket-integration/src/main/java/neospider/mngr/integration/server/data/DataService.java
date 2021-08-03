package neospider.mngr.integration.server.data;

import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.Map;

@Service
public class DataService {

    // try to check java doc this each lines. Must be easy to understand than my words :D
    final Sinks.Many<Map> many = Sinks.many()
            .multicast()
            .onBackpressureBuffer(100);

    // push data coming from stock broker here. Check DataGenerator class for understanding more easily.
    public void onNext(Map next) {
        many.emitNext(next, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public Flux<Map> getMessages() {
        return many.asFlux();
    }

}
