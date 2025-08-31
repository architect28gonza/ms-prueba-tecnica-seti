package co.com.franquicia.api.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Constants;
import co.com.franquicia.api.handler.producto.ProductoHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductoRouterRestTest {

    private WebTestClient webTestClient;
    private co.com.franquicia.api.handler.producto.ProductoHandler handler;

    @BeforeEach
    void setUp() {
        handler = Mockito.mock(ProductoHandler.class);

        RouterFunction<ServerResponse> routerFunction = new ProductoRouterRest().routerPorducto(handler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testAddProducto() {
        when(handler.add(any())).thenReturn(ServerResponse.ok().bodyValue("Producto agregado"));

        webTestClient.post()
                .uri(Constants.PATH + "producto")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Producto agregado");
    }

    @Test
    void testListProducto() {
        when(handler.list(any())).thenReturn(ServerResponse.ok().bodyValue("Listado de productos"));

        webTestClient.get()
                .uri(Constants.PATH + "producto/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Listado de productos");
    }

    @Test
    void testDeleteProducto() {
        when(handler.delete(any())).thenReturn(ServerResponse.noContent().build());

        webTestClient.delete()
                .uri(Constants.PATH + "producto/99")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateStockProducto() {
        when(handler.updateStock(any())).thenReturn(ServerResponse.ok().bodyValue("Stock actualizado"));

        webTestClient.put()
                .uri(Constants.PATH + "producto")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Stock actualizado");
    }
}