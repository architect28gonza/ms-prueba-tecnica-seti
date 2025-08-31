package co.com.franquicia.api.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.handler.producto.ProductoHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductoRouterRest {

    @Bean
    RouterFunction<ServerResponse> routerPorducto(ProductoHandler handler) {
        return route(POST(Constants.PATH.concat("producto")), handler::add)
                .andRoute(GET(Constants.PATH.concat("producto/{id_franquicia}")), handler::list)
                .andRoute(DELETE(Constants.PATH.concat("producto/{id_producto}")), handler::delete)
                .andRoute(PUT(Constants.PATH.concat("producto")), handler::updateStock);
    }
}
