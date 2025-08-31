package co.com.franquicia.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.handler.sucursal.SucursalHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SucursalRouterRest {

    @Bean
    RouterFunction<ServerResponse> routerSucursal(SucursalHandler handler) {
        return route(POST(Constants.PATH.concat("sucursal")), handler::add)
                .andRoute(PUT(Constants.PATH.concat("sucursal")), handler::updateName);
    }
}
