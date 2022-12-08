package com.van.mngr.integration.server.data;

import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.Map;

@Service
public class DataService {

    private EmitterProcessor<Map> processor = EmitterProcessor.create();

    public void onNext(Map next) {
        processor.onNext(next);
    }

    public Flux<Map> getMessages() {
        return processor.publish().autoConnect();
    }

}
