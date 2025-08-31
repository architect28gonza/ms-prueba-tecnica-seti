package co.com.franquicia.api.router;

import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.handler.franquicia.FranquiciaHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FranquiciaRouterRestTest {

    private WebTestClient webTestClient;
    private FranquiciaHandler handler;

    @BeforeEach
    void setUp() {
        handler = Mockito.mock(FranquiciaHandler.class);

        RouterFunction<ServerResponse> routerFunction = new FranquiciaRouterRest().routerFranquicia(handler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testAddFranquicia() {
        when(handler.add(any())).thenReturn(ServerResponse.ok().bodyValue("Franquicia agregada"));

        webTestClient.get()
                .uri(Constants.PATH + "franquicia/MiFranquicia")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Franquicia agregada");
    }

    @Test
    void testUpdateFranquiciaName() {
        when(handler.updateName(any())).thenReturn(ServerResponse.ok().bodyValue("Nombre actualizado"));

        webTestClient.put()
                .uri(Constants.PATH + "franquicia")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Nombre actualizado");
    }
}