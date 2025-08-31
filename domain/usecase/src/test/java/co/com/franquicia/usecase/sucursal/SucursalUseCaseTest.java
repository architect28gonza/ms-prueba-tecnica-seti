package co.com.franquicia.usecase.sucursal;

import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.model.franquicia.gateways.FranquiciaRepository;
import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.model.sucursal.gateways.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SucursalUseCaseTest {

    private SucursalRepository sucursalRepository;
    private FranquiciaRepository franquiciaRepository;
    private SucursalUseCase sucursalUseCase;

    @BeforeEach
    void setUp() {
        sucursalRepository = mock(SucursalRepository.class);
        franquiciaRepository = mock(FranquiciaRepository.class);
        sucursalUseCase = new SucursalUseCase(sucursalRepository, franquiciaRepository);
    }

    @Test
    void saveSucursal_Success() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);

        Sucursal sucursal = new Sucursal();
        sucursal.setFranquicia(franquicia);
        sucursal.setNombre("Sucursal A");

        when(franquiciaRepository.findByIdFranquicia(1L)).thenReturn(Mono.just(franquicia));
        when(sucursalRepository.saveSucursal(any(Sucursal.class))).thenAnswer(i -> Mono.just(i.getArgument(0)));

        StepVerifier.create(sucursalUseCase.save(sucursal))
                .expectNextMatches(saved -> saved.getNombre().equals("Sucursal A") &&
                        saved.getFranquicia() != null &&
                        saved.getFranquicia().getId().equals(1L))
                .verifyComplete();

        verify(franquiciaRepository).findByIdFranquicia(1L);
        verify(sucursalRepository).saveSucursal(any(Sucursal.class));
    }

    @Test
    void updateNombre_Success() {
        Franquicia franquicia = new Franquicia();
        franquicia.setId(1L);

        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setFranquicia(franquicia);
        sucursal.setNombre("Sucursal Antiguo");

        Sucursal sucursalActualizada = new Sucursal();
        sucursalActualizada.setId(1L);
        sucursalActualizada.setNombre("Sucursal Nuevo");

        when(sucursalRepository.findByIdSucursal(1L)).thenReturn(Mono.just(sucursal));
        when(sucursalRepository.updateNombreSucursal(1L, "Sucursal Nuevo"))
                .thenReturn(Mono.just(sucursalActualizada));

        StepVerifier.create(sucursalUseCase.updateNombre(1L, "Sucursal Nuevo"))
                .expectNextMatches(updated -> updated.getNombre().equals("Sucursal Nuevo") &&
                        updated.getFranquicia().getId().equals(1L))
                .verifyComplete();

        verify(sucursalRepository).findByIdSucursal(1L);
        verify(sucursalRepository).updateNombreSucursal(1L, "Sucursal Nuevo");
    }
}
