package neospider.mngr.integration.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableIntegration
@Slf4j
public class SpringIntegrationSseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationSseDemoApplication.class, args);
    }

}
