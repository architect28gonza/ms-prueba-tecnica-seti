package co.com.franquicia.api.handler;

import co.com.franquicia.api.common.constants.Message;
import co.com.franquicia.api.dto.request.franquicia.UpdateNameDTO;
import co.com.franquicia.api.handler.franquicia.FranquiciaHandler;
import co.com.franquicia.api.validator.Validator;
import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.usecase.franquicia.FranquiciaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(MockitoExtension.class)
class FranquiciaHandlerTest {

    @Mock
    private FranquiciaUseCase useCase;

    @Mock
    private Validator validator;

    @InjectMocks
    private FranquiciaHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<ServerResponse> router = route(RequestPredicates.POST("/franquicia/{nombre_franquicia}"),
                handler::add)
                .andRoute(RequestPredicates.PUT("/franquicia/update"), handler::updateName);

        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    @Test
    void addFranquicia_ok() {
        String nombreFranquicia = "MiFranquicia";

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(nombreFranquicia);

        when(useCase.save(nombreFranquicia)).thenReturn(Mono.just(franquicia));

        webTestClient.post().uri("/franquicia/{nombre_franquicia}", nombreFranquicia)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }
    
    @Test
    void updateName_ok() {
        UpdateNameDTO dto = new UpdateNameDTO(1L, "NuevoNombre");
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);
        franquicia.setNombre("NuevoNombre");

        when(validator.validateDto(any(UpdateNameDTO.class))).thenReturn(Mono.just(dto));
        when(useCase.updateName(dto.id(), dto.nombre())).thenReturn(Mono.just(franquicia));

        webTestClient.put().uri("/franquicia/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(res -> {
                    String body = res.getResponseBody();
                    assert body != null && body.contains(Message.MESSAGE_HANDLER_UPDATE_NAME_FRANQUICIA);
                });
    }
}
