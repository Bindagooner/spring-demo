package neospider.mngr.integration.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 *
 */
@Configuration
@EnableWebFlux
class WebConfig implements WebFluxConfigurer {
    // nothing here for now. this makes application run as war file
}
