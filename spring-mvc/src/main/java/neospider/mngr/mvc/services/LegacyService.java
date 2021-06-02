package neospider.mngr.mvc.services;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import neospider.mngr.mvc.clients.LegacyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class LegacyService {

    private LegacyClient client;

    public LegacyService(@Value("${legacy-service.url}") String url) {
        client = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(LegacyClient.class))
                .logLevel(Logger.Level.FULL)
                .target(LegacyClient.class, url);
    }

    public List<String> listAll() {
        Map<String, List<String>> response = client.listAll();
        return response.get("data");
    }

    public String saveBook(String book) {
        Map<String, String> response = client.saveBook(Collections.singletonMap("book", "{}"));
        return response.get("data");
    }
}
