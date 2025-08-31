package co.com.franquicia.api.router;


import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.handler.sucursal.SucursalHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SucursalRouterRestTest {

    private WebTestClient webTestClient;
    private SucursalHandler handler;

    @BeforeEach
    void setUp() {
        handler = Mockito.mock(SucursalHandler.class);

        RouterFunction<ServerResponse> routerFunction =
                new SucursalRouterRest().routerSucursal(handler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testAddSucursal() {
        when(handler.add(any())).thenReturn(ServerResponse.ok().bodyValue("Sucursal agregada"));

        webTestClient.post()
                .uri(Constants.PATH + "sucursal")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Sucursal agregada");
    }

    @Test
    void testUpdateSucursalName() {
        when(handler.updateName(any())).thenReturn(ServerResponse.ok().bodyValue("Nombre sucursal actualizado"));

        webTestClient.put()
                .uri(Constants.PATH + "sucursal")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Nombre sucursal actualizado");
    }
}