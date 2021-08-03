package neospider.mngr.integration.server.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class ReactiveWebConfiguration {

    // config router
    @Bean
    public RouterFunction<ServerResponse> htmlRouter(@Value("classpath:/static/index.html") Resource html) {
        return route(
                GET("/"),
                request -> ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(html)
        ).andRoute(GET("index.html"),
                request -> ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(html));
    }

    // register WebSocket endpoint.
    @Bean
    public HandlerMapping handlerMapping(IntegrationFlowWebSocketHandler integrationFlowWebSocketHandler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/time/**", integrationFlowWebSocketHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(-1); // before annotated controllers
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public WebFluxProperties webFluxProperties(){
        return new WebFluxProperties();
    }
}
