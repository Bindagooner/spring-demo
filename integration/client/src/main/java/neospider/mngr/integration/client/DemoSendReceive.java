package neospider.mngr.integration.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@Slf4j
@EnableScheduling
public class DemoSendReceive {

    Executor executor = Executors.newFixedThreadPool(10);
    WebClient client = WebClient.create("http://localhost:8080");

    private void execute() {
        Map<String, String> request = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();

            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            request.put(String.format("key-%s", i), generatedString);

        }
        log.info("sending request: {}", request);
        client.post().uri("/sse")
                .body(Mono.just(request), Map.class)
                .retrieve()
                .bodyToFlux(Map.class)
                .subscribe(MessageReceiver::handle);
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void run() {
        log.info("job started");
        for (int i = 0; i < 20; i++) {
            executor.execute(this::execute);
        }
    }
}
