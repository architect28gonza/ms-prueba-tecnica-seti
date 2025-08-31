package co.com.franquicia.api.handler.franquicia;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface FranquiciaHandlerContract {

    Mono<ServerResponse> add(ServerRequest serverRequest);

    Mono<ServerResponse> updateName(ServerRequest serverRequest);

}
