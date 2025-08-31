package co.com.franquicia.api.handler.producto;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface ProductoHandlerContract {

    Mono<ServerResponse> add(ServerRequest serverRequest);

    Mono<ServerResponse> delete(ServerRequest serverRequest);

    Mono<ServerResponse> updateStock(ServerRequest serverRequest);

    Mono<ServerResponse> list(ServerRequest serverRequest);
}