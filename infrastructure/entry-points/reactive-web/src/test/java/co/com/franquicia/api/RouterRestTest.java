package co.com.franquicia.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.handler.producto.ProductoHandler;
import co.com.franquicia.api.router.ProductoRouterRest;
import reactor.blockhound.BlockHound;

@ContextConfiguration(classes = { ProductoRouterRest.class, ProductoHandler.class })
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    private ProductoHandler handler;

    // @BeforeEach
    // void setUp() {
    //      BlockHound.disable();
    //     handler = Mockito.mock(ProductoHandler.class);
    //     Mockito.when(handler.add(Mockito.any())).thenReturn(ServerResponse.ok().build());
    //     Mockito.when(handler.list(Mockito.any())).thenReturn(ServerResponse.ok().bodyValue("[]"));
    //     Mockito.when(handler.delete(Mockito.any())).thenReturn(ServerResponse.ok().build());
    //     Mockito.when(handler.updateStock(Mockito.any())).thenReturn(ServerResponse.ok().build());
    // }

    @Test
    void testAddProducto() {
        webTestClient.post()
                .uri("/api/producto")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\":\"Test\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).isEmpty());
    }

    @Test
    void testListProductos() {
        webTestClient.get()
                .uri("/api/producto/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).isEqualTo("[]"));
    }

    @Test
    void testDeleteProducto() {
        webTestClient.delete()
                .uri("/api/producto/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).isEmpty());
    }

    @Test
    void testUpdateStockProducto() {
        webTestClient.put()
                .uri("/api/producto")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"id\":1,\"stock\":100}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).isEmpty());
    }
}
