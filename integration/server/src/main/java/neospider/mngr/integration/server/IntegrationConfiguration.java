package neospider.mngr.integration.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.webflux.dsl.WebFlux;

import java.util.Map;

@Configuration
public class IntegrationConfiguration {
    @Bean
    public IntegrationFlow sseFlow() {
        return IntegrationFlows
                .from(WebFlux.inboundGateway("/sse")
                        .requestMapping(m -> {
                            m.produces(MediaType.TEXT_EVENT_STREAM_VALUE);
                            m.methods(HttpMethod.POST);
                        }))
                .convert(Map.class)
                .handle(MessageHandler::upperCaseValue)
                .get();
    }
}
