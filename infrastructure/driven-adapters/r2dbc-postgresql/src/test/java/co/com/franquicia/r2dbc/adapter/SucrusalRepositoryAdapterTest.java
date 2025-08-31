package co.com.franquicia.r2dbc.adapter;

import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.model.sucursal.gateways.SucursalRepository;
import co.com.franquicia.r2dbc.repository.sucursal.SucrusalRepositoryAdapter;
import co.com.franquicia.r2dbc.repository.sucursal.SucursalEntity;
import co.com.franquicia.r2dbc.repository.sucursal.SucursalReactiveRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class SucrusalRepositoryAdapterTest {

    private SucursalReactiveRepository reactiveRepository;
    private ObjectMapper objectMapper;
    private SucursalRepository repository;

    @BeforeEach
    void setUp() {
        reactiveRepository = mock(SucursalReactiveRepository.class);
        objectMapper = mock(ObjectMapper.class);
        repository = new SucrusalRepositoryAdapter(reactiveRepository, objectMapper);
    }

    @Test
    void testSaveSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal 1");
        sucursal.setFranquicia(new co.com.franquicia.model.franquicia.Franquicia());
        sucursal.getFranquicia().setId(1L);

        SucursalEntity entity = new SucursalEntity();
        entity.setId(1L);
        entity.setNombre("Sucursal 1");

        when(reactiveRepository.save("Sucursal 1", 1L)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Sucursal.class)).thenReturn(sucursal);

        StepVerifier.create(repository.saveSucursal(sucursal))
                .expectNextMatches(result -> result.getNombre().equals("Sucursal 1"))
                .verifyComplete();

        verify(reactiveRepository).save("Sucursal 1", 1L);
    }

    @Test
    void testFindByIdSucursal() {
        SucursalEntity entity = new SucursalEntity();
        entity.setId(1L);
        entity.setNombre("Sucursal 1");

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal 1");

        when(reactiveRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Sucursal.class)).thenReturn(sucursal);

        StepVerifier.create(repository.findByIdSucursal(1L))
                .expectNextMatches(result -> result.getNombre().equals("Sucursal 1"))
                .verifyComplete();

        verify(reactiveRepository).findById(1L);
    }

    @Test
    void testUpdateNombreSucursal() {
        SucursalEntity entity = new SucursalEntity();
        entity.setId(1L);
        entity.setNombre("Sucursal Actualizada");

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal Actualizada");

        when(reactiveRepository.updateNombre(1L, "Sucursal Actualizada")).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Sucursal.class)).thenReturn(sucursal);

        StepVerifier.create(repository.updateNombreSucursal(1L, "Sucursal Actualizada"))
                .expectNextMatches(result -> result.getNombre().equals("Sucursal Actualizada"))
                .verifyComplete();

        verify(reactiveRepository).updateNombre(1L, "Sucursal Actualizada");
    }

    @Test
    void testFindAllById() {
        SucursalEntity entity1 = new SucursalEntity();
        entity1.setId(1L);
        entity1.setNombre("Sucursal 1");

        SucursalEntity entity2 = new SucursalEntity();
        entity2.setId(2L);
        entity2.setNombre("Sucursal 2");

        Sucursal sucursal1 = new Sucursal();
        sucursal1.setNombre("Sucursal 1");
        Sucursal sucursal2 = new Sucursal();
        sucursal2.setNombre("Sucursal 2");

        when(reactiveRepository.findByIds(List.of(1L, 2L)))
                .thenReturn(Flux.just(entity1, entity2));
        when(objectMapper.map(entity1, Sucursal.class)).thenReturn(sucursal1);
        when(objectMapper.map(entity2, Sucursal.class)).thenReturn(sucursal2);

        StepVerifier.create(repository.findAllById(List.of(1L, 2L)))
                .expectNextMatches(s -> s.getNombre().equals("Sucursal 1"))
                .expectNextMatches(s -> s.getNombre().equals("Sucursal 2"))
                .verifyComplete();

        verify(reactiveRepository).findByIds(List.of(1L, 2L));
    }
}