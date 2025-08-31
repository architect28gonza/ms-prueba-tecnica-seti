package co.com.franquicia.api.handler;

import co.com.franquicia.api.common.constants.Message;
import co.com.franquicia.api.dto.request.IdSucursalDTO;
import co.com.franquicia.api.dto.request.producto.ProductoDTO;
import co.com.franquicia.api.dto.request.producto.UpdateStockDTO;
import co.com.franquicia.api.handler.producto.ProductoHandler;
import co.com.franquicia.api.validator.ProductoValidator;
import co.com.franquicia.api.validator.Validator;
import co.com.franquicia.model.producto.Producto;
import co.com.franquicia.usecase.producto.ProductoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import co.com.franquicia.model.sucursal.Sucursal;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ExtendWith(MockitoExtension.class)
class ProductoHandlerTest {

    @Mock
    private ProductoUseCase productoUseCase;

    @Mock
    private ProductoValidator productoValidator;

    @Mock
    private Validator validator;

    @InjectMocks
    private ProductoHandler handler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterFunction<ServerResponse> router = route(RequestPredicates.POST("/producto"), handler::add)
                .andRoute(RequestPredicates.DELETE("/producto/{id_producto}"), handler::delete)
                .andRoute(RequestPredicates.PUT("/producto"), handler::updateStock);

        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    @Test
    void addProducto_ok() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        ProductoDTO dto = new ProductoDTO("Camisa", 15, new IdSucursalDTO(1L));

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Camisa");
        producto.setStock(15);
        producto.setSucursal(sucursal);

        when(productoValidator.validate(any(ProductoDTO.class))).thenReturn(Mono.just(producto));
        when(productoUseCase.save(any(Producto.class))).thenReturn(Mono.just(producto));

        webTestClient.post().uri("/producto")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(res -> {
                    String body = res.getResponseBody();
                    assert body != null && body.contains(Message.MESSAGE_HANDLER_PRODUCTO);
                });
    }

    @Test
    void deleteProducto_ok() {
        when(productoUseCase.delete(anyLong())).thenReturn(Mono.empty());
        webTestClient.delete().uri("/producto/{id_producto}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(res -> {
                    String body = res.getResponseBody();
                    assert body != null && body.contains(Message.MESSAGE_HANDLER_DELETE_PRODUCTO);
                });
    }

    @Test
    void updateStock_ok() {
        UpdateStockDTO dto = new UpdateStockDTO(1L, 30);
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(30);

        when(validator.validateDto(any(UpdateStockDTO.class))).thenReturn(Mono.just(dto));
        when(productoUseCase.update(dto.id(), dto.stock())).thenReturn(Mono.just(producto));

        webTestClient.put().uri("/producto")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(res -> {
                    String body = res.getResponseBody();
                    assert body != null && body.contains(Message.MESSAGE_HANDLER_UPDATE_STOCK_PRODUCTO);
                });
    }

    @Test
    void testListConIdValido() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("id_franquicia")).thenReturn("1");

        Producto prod1 = new Producto();
        prod1.setId(1l);
        prod1.setNombre("prod1");
        prod1.setStock(40);
        when(productoUseCase.listStockMax(1L)).thenReturn(Flux.just(prod1));

        Mono<ServerResponse> responseMono = handler.list(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(resp -> resp.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(productoUseCase).listStockMax(1L);
    }

    @Test
    void testListConIdInvalido() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("id_franquicia")).thenReturn("-1");
        Mono<ServerResponse> responseMono = handler.list(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(resp -> resp.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verifyNoInteractions(productoUseCase);
    }
}