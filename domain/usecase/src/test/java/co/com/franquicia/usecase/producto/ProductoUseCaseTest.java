package co.com.franquicia.usecase.producto;

import co.com.franquicia.model.producto.Producto;
import co.com.franquicia.model.producto.gateways.ProductoRepository;
import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.model.sucursal.gateways.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductoUseCaseTest {

    private ProductoRepository productoRepository;
    private SucursalRepository sucursalRepository;
    private ProductoUseCase productoUseCase;

    @BeforeEach
    void setUp() {
        productoRepository = mock(ProductoRepository.class);
        sucursalRepository = mock(SucursalRepository.class);
        productoUseCase = new ProductoUseCase(productoRepository, sucursalRepository);
    }

    @Test
    void save_whenSucursalExists_shouldSaveProducto() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        Producto producto = new Producto();
        producto.setSucursal(sucursal);

        when(sucursalRepository.findByIdSucursal(1L)).thenReturn(Mono.just(sucursal));
        when(productoRepository.saveProducto(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(productoUseCase.save(producto))
                .expectNextMatches(p -> p.getSucursal().getId().equals(1L))
                .verifyComplete();

        verify(productoRepository).saveProducto(any(Producto.class));
    }

    @Test
    void save_whenSucursalDoesNotExist_shouldReturnError() {
        Producto producto = new Producto();
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        producto.setSucursal(sucursal);

        when(sucursalRepository.findByIdSucursal(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.save(producto))
                .expectErrorMessage("No existe el sucursal")
                .verify();

        verify(productoRepository, never()).saveProducto(any());
    }

    @Test
    void delete_whenProductoExists_shouldDelete() {
        Producto producto = new Producto();
        producto.setId(1L);

        when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepository.deleteProsducto(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.delete(1L))
                .verifyComplete();

        verify(productoRepository).deleteProsducto(1L);
    }

    @Test
    void delete_whenProductoDoesNotExist_shouldReturnError() {
        when(productoRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.delete(1L))
                .expectErrorMessage("El producto no existe")
                .verify();

        verify(productoRepository, never()).deleteProsducto(any());
    }

    @Test
    void update_whenProductoExists_shouldUpdateStock() {
        Producto producto = new Producto();
        producto.setId(1L);

        when(productoRepository.findById(1L)).thenReturn(Mono.just(producto));
        when(productoRepository.updateProducto(1L, 10)).thenReturn(Mono.just(producto));

        StepVerifier.create(productoUseCase.update(1L, 10))
                .expectNext(producto)
                .verifyComplete();
    }

    @Test
    void update_whenProductoDoesNotExist_shouldReturnError() {
        when(productoRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productoUseCase.update(1L, 10))
                .expectErrorMessage("El producto no existe")
                .verify();

        verify(productoRepository, never()).updateProducto(anyLong(), anyInt());
    }

    @Test
    void listStockMax_shouldReturnProductosWithSucursal() {
        Producto producto = new Producto();
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        producto.setSucursal(sucursal);

        when(productoRepository.findProductosConMaxStockPorFranquicia(1L)).thenReturn(Flux.just(producto));
        when(sucursalRepository.findByIdSucursal(1L)).thenReturn(Mono.just(sucursal));

        StepVerifier.create(productoUseCase.listStockMax(1L))
                .expectNextMatches(p -> p.getSucursal().getId().equals(1L))
                .verifyComplete();
    }
}