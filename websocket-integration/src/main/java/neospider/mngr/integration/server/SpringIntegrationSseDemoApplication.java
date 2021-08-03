package neospider.mngr.integration.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;


@SpringBootApplication
@EnableIntegration
public class SpringIntegrationSseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationSseDemoApplication.class, args);
    }
}
