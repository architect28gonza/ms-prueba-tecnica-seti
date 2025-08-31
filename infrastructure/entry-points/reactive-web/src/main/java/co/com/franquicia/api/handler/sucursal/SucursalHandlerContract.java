package co.com.franquicia.api.handler.sucursal;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface SucursalHandlerContract {

    Mono<ServerResponse> add(ServerRequest serverRequest);

    Mono<ServerResponse> updateName(ServerRequest serverRequest);
}
