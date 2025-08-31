package co.com.franquicia.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.handler.franquicia.FranquiciaHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranquiciaRouterRest {

    @Bean
    RouterFunction<ServerResponse> routerFranquicia(FranquiciaHandler handler) {
        return route(GET(Constants.PATH.concat("franquicia/{nombre_franquicia}")), handler::add)
        .andRoute(PUT(Constants.PATH.concat("franquicia")), handler::updateName);
    }
}
