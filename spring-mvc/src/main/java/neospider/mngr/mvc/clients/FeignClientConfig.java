package neospider.mngr.mvc.clients;

import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.ssl.SSLSocketFactory;

@Configuration
public class FeignClientConfig {

    @Bean
    public BusinessTierClient buildClient(@Value("${business-tier.url}") String url, @Autowired SSLSocketFactory sslSocketFactory) {
       return Feign.builder()
                .client(new Client.Default(sslSocketFactory, new DefaultHostnameVerifier()))
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(BusinessTierClient.class))
                .logLevel(Logger.Level.FULL)
                .target(BusinessTierClient.class, url);
    }
}
