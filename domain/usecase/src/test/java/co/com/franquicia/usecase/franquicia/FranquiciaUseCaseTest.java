package co.com.franquicia.usecase.franquicia;


import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.model.franquicia.gateways.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FranquiciaUseCaseTest {

    private FranquiciaRepository franquiciaRepository;
    private FranquiciaUseCase franquiciaUseCase;

    @BeforeEach
    void setUp() {
        franquiciaRepository = mock(FranquiciaRepository.class);
        franquiciaUseCase = new FranquiciaUseCase(franquiciaRepository);
    }

    @Test
    void save_whenNombreDoesNotExist_shouldSaveFranquicia() {
        String nombre = "Franquicia1";
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre(nombre);

        when(franquiciaRepository.existsByNombre(nombre)).thenReturn(Mono.just(false));
        when(franquiciaRepository.saveFranquicia(nombre)).thenReturn(Mono.just(franquicia));

        StepVerifier.create(franquiciaUseCase.save(nombre))
                .expectNextMatches(f -> f.getNombre().equals(nombre))
                .verifyComplete();

        verify(franquiciaRepository).saveFranquicia(nombre);
    }

    @Test
    void save_whenNombreExists_shouldReturnError() {
        String nombre = "Franquicia1";

        when(franquiciaRepository.existsByNombre(nombre)).thenReturn(Mono.just(true));

        StepVerifier.create(franquiciaUseCase.save(nombre))
                .expectErrorMessage("La franquicia ya existe")
                .verify();

        verify(franquiciaRepository, never()).saveFranquicia(any());
    }

    @Test
    void updateName_whenNombreAlreadyExists_shouldReturnError() {
        Long id = 1L;
        String nuevoNombre = "NombreExistente";
        Franquicia franquicia = new Franquicia();
        franquicia.setId(id);

        when(franquiciaRepository.findByIdFranquicia(id)).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.existsByNombre(nuevoNombre)).thenReturn(Mono.just(true));

        StepVerifier.create(franquiciaUseCase.updateName(id, nuevoNombre))
                .expectErrorMessage("El nombre de la franquicia ya se encuentra registrado")
                .verify();

        verify(franquiciaRepository, never()).updateNombreFranquicia(anyLong(), any());
    }
}