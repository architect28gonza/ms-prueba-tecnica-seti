package co.com.franquicia.r2dbc.adapter;

import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.r2dbc.repository.franquicia.FranquiciaEntity;
import co.com.franquicia.r2dbc.repository.franquicia.FranquiciaReactiveRepository;
import co.com.franquicia.r2dbc.repository.franquicia.FranquiciaRepositoryAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class FranquiciaRepositoryAdapterTest {

    private FranquiciaReactiveRepository reactiveRepository;
    private ObjectMapper objectMapper;
    private FranquiciaRepositoryAdapter repository;

    @BeforeEach
    void setUp() {
        reactiveRepository = mock(FranquiciaReactiveRepository.class);
        objectMapper = mock(ObjectMapper.class);
        repository = new FranquiciaRepositoryAdapter(reactiveRepository, objectMapper);
    }

    @Test
    void testSaveFranquicia() {
        FranquiciaEntity entity = new FranquiciaEntity();
        entity.setId(1L);
        entity.setNombre("Franquicia 1");

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Franquicia 1");

        when(reactiveRepository.save("Franquicia 1")).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Franquicia.class)).thenReturn(franquicia);

        StepVerifier.create(repository.saveFranquicia("Franquicia 1"))
                .expectNextMatches(f -> f.getNombre().equals("Franquicia 1"))
                .verifyComplete();

        verify(reactiveRepository).save("Franquicia 1");
    }

    @Test
    void testExistsByNombre() {
        when(reactiveRepository.existsByNombre("Franquicia 1")).thenReturn(Mono.just(true));

        StepVerifier.create(repository.existsByNombre("Franquicia 1"))
                .expectNext(true)
                .verifyComplete();

        verify(reactiveRepository).existsByNombre("Franquicia 1");
    }

    @Test
    void testFindByIdFranquicia() {
        FranquiciaEntity entity = new FranquiciaEntity();
        entity.setId(1L);
        entity.setNombre("Franquicia X");

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Franquicia X");

        when(reactiveRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Franquicia.class)).thenReturn(franquicia);

        StepVerifier.create(repository.findByIdFranquicia(1L))
                .expectNextMatches(f -> f.getNombre().equals("Franquicia X"))
                .verifyComplete();

        verify(reactiveRepository).findById(1L);
    }

    @Test
    void testUpdateNombreFranquicia() {
        FranquiciaEntity entity = new FranquiciaEntity();
        entity.setId(1L);
        entity.setNombre("Franquicia Actualizada");

        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Franquicia Actualizada");

        when(reactiveRepository.updateNombre(1L, "Franquicia Actualizada")).thenReturn(Mono.just(entity));
        when(objectMapper.map(entity, Franquicia.class)).thenReturn(franquicia);

        StepVerifier.create(repository.updateNombreFranquicia(1L, "Franquicia Actualizada"))
                .expectNextMatches(f -> f.getNombre().equals("Franquicia Actualizada"))
                .verifyComplete();

        verify(reactiveRepository).updateNombre(1L, "Franquicia Actualizada");
    }
}