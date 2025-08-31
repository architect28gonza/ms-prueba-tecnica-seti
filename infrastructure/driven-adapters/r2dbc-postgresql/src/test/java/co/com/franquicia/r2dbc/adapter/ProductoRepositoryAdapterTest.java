package co.com.franquicia.r2dbc.adapter;

import co.com.franquicia.model.producto.Producto;
import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.r2dbc.repository.producto.ProductoEntity;
import co.com.franquicia.r2dbc.repository.producto.ProductoReactiveRepository;
import co.com.franquicia.r2dbc.repository.producto.ProductoRepositoryAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ProductoRepositoryAdapterTest {

    private ProductoReactiveRepository reactiveRepository;
    private ObjectMapper objectMapper;
    private ProductoRepositoryAdapter repository;

    @BeforeEach
    void setUp() {
        reactiveRepository = mock(ProductoReactiveRepository.class);
        objectMapper = mock(ObjectMapper.class);
        repository = new ProductoRepositoryAdapter(reactiveRepository, objectMapper);
    }

    @Test
    void testSaveProducto() {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setStock(10);
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        producto.setSucursal(sucursal);

        ProductoEntity entity = new ProductoEntity();
        entity.setId(1L);
        entity.setNombre("Producto 1");
        entity.setStock(10);

        when(reactiveRepository.save("Producto 1", 10, 1L)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Producto.class)).thenReturn(producto);

        StepVerifier.create(repository.saveProducto(producto))
                .expectNextMatches(p -> p.getNombre().equals("Producto 1") && p.getStock() == 10)
                .verifyComplete();

        verify(reactiveRepository).save("Producto 1", 10, 1L);
    }

    @Test
    void testDeleteProducto() {
        when(reactiveRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.deleteProsducto(1L))
                .verifyComplete();

        verify(reactiveRepository).deleteById(1L);
    }

    @Test
    void testFindById() {
        ProductoEntity entity = new ProductoEntity();
        entity.setId(1L);
        entity.setNombre("Producto 1");
        entity.setStock(10);

        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        producto.setStock(10);

        when(reactiveRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Producto.class)).thenReturn(producto);

        StepVerifier.create(repository.findById(1L))
                .expectNextMatches(p -> p.getNombre().equals("Producto 1") && p.getStock() == 10)
                .verifyComplete();

        verify(reactiveRepository).findById(1L);
    }

    @Test
    void testUpdateProducto() {
        ProductoEntity entity = new ProductoEntity();
        entity.setId(1L);
        entity.setNombre("Producto 1");
        entity.setStock(20);

        Producto producto = new Producto();
        producto.setStock(20);

        when(reactiveRepository.updateStock(1L, 20)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Producto.class)).thenReturn(producto);

        StepVerifier.create(repository.updateProducto(1L, 20))
                .expectNextMatches(p -> p.getStock() == 20)
                .verifyComplete();

        verify(reactiveRepository).updateStock(1L, 20);
    }

    @Test
    void testFindProductosConMaxStockPorFranquicia() {
        ProductoEntity entity1 = new ProductoEntity();
        entity1.setId(1L);
        entity1.setNombre("Producto A");
        entity1.setStock(50);

        ProductoEntity entity2 = new ProductoEntity();
        entity2.setId(2L);
        entity2.setNombre("Producto B");
        entity2.setStock(50);

        when(reactiveRepository.findProductosConMaxStockPorFranquicia(1L))
                .thenReturn(Flux.just(entity1, entity2));

        StepVerifier.create(repository.findProductosConMaxStockPorFranquicia(1L))
                .expectNextMatches(p -> p.getNombre().equals("Producto A") && p.getStock() == 50)
                .expectNextMatches(p -> p.getNombre().equals("Producto B") && p.getStock() == 50)
                .verifyComplete();

        verify(reactiveRepository).findProductosConMaxStockPorFranquicia(1L);
    }
}