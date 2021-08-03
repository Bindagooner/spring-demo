package neospider.mngr.integration.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.web.reactive.config.EnableWebFlux;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableIntegration
@EnableWebFlux
@EnableIntegrationManagement(defaultLoggingEnabled = "false")
public class SpringIntegrationSseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationSseDemoApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//    	return application.sources(SpringIntegrationSseDemoApplication.class);
//    }

    //출처: https://oingdaddy.tistory.com/346?category=824422 [SI Supply Depot]
}
