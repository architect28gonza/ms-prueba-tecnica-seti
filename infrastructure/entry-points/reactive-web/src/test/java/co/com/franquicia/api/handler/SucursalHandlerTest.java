package co.com.franquicia.api.handler;


import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.common.constants.Message;
import co.com.franquicia.api.dto.request.IdFranquiciaDTO;
import co.com.franquicia.api.dto.request.sucursal.SucursalDTO;
import co.com.franquicia.api.dto.request.sucursal.UpdateNameDTO;
import co.com.franquicia.api.handler.sucursal.SucursalHandler;
import co.com.franquicia.api.validator.SucursalValidator;
import co.com.franquicia.api.validator.Validator;
import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.usecase.sucursal.SucursalUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(MockitoExtension.class)
class SucursalHandlerTest {

    @Mock
    private SucursalUseCase sucursalUseCase;

    @Mock
    private SucursalValidator validatorSucursal;

    @Mock
    private Validator validator;

    @InjectMocks
    private SucursalHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<ServerResponse> router = route()
                .POST(Constants.PATH.concat("sucursal"), handler::add)
                .PUT(Constants.PATH.concat("sucursal"), handler::updateName)
                .build();

        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    @Test
    void addSucursal_ok() {
        SucursalDTO dto = new SucursalDTO("Sucursal Centro", new IdFranquiciaDTO(1L));
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre(dto.nombre());
        sucursal.setFranquicia(new Franquicia());

        when(validatorSucursal.validate(any(SucursalDTO.class))).thenReturn(Mono.just(sucursal));
        when(sucursalUseCase.save(sucursal)).thenReturn(Mono.just(sucursal));

        webTestClient.post().uri(Constants.PATH.concat("sucursal"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(res -> {
                    String body = res.getResponseBody();
                    assert body != null && body.contains(Message.MESSAGE_HANDLER_OK_SUCURSAL);
                });
    }

    @Test
    void updateNameSucursal_ok() {
        UpdateNameDTO dto = new UpdateNameDTO(1L, "Sucursal Norte");
        Sucursal sucursal = new Sucursal();
        sucursal.setId(dto.id());
        sucursal.setNombre(dto.nombre());

        when(validator.validateDto(any(UpdateNameDTO.class))).thenReturn(Mono.just(dto));
        when(sucursalUseCase.updateNombre(dto.id(), dto.nombre())).thenReturn(Mono.just(sucursal));

        webTestClient.put().uri(Constants.PATH.concat("sucursal"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(res -> {
                    String body = res.getResponseBody();
                    assert body != null && body.contains(Message.MESSAGE_HANDLER_UPDATE_NAME_SUCURSAL);
                });
    }
}