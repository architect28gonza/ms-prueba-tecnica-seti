package co.com.franquicia.api.validator;

import co.com.franquicia.api.dto.request.IdFranquiciaDTO;
import co.com.franquicia.api.dto.request.sucursal.SucursalDTO;
import co.com.franquicia.api.exceptions.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test unitario para SucursalValidator
 */
class SucursalValidatorTest {

    private SmartValidator smartValidator;
    private SucursalValidator sucursalValidator;

    @BeforeEach
    void setUp() {
        smartValidator = mock(SmartValidator.class);
        sucursalValidator = new SucursalValidator(smartValidator);
    }

    @Test
    void testValidate_validSucursalDTO() {
        IdFranquiciaDTO idFranquiciaDTO = new IdFranquiciaDTO(1L);
        SucursalDTO dto = new SucursalDTO("Sucursal Central", idFranquiciaDTO);

        doAnswer(invocation -> null)
                .when(smartValidator).validate(eq(dto), any(Errors.class));

        StepVerifier.create(sucursalValidator.validate(dto))
                .assertNext(sucursal -> {
                    assertEquals("Sucursal Central", sucursal.getNombre());
                    assertNotNull(sucursal.getFranquicia());
                    assertEquals(1L, sucursal.getFranquicia().getId());
                })
                .verifyComplete();

        verify(smartValidator).validate(eq(dto), any(Errors.class));
    }

    @Test
    void testValidate_invalidSucursalDTO() {
        SucursalDTO dto = new SucursalDTO("", null);

        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.reject("400", "Nombre inválido");
            return null;
        }).when(smartValidator).validate(eq(dto), any(Errors.class));

        StepVerifier.create(sucursalValidator.validate(dto))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof ApiException);
                    ApiException ex = (ApiException) error;
                    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
                    assertTrue(ex.getMessage().contains("Nombre inválido"));
                })
                .verify();

        verify(smartValidator).validate(eq(dto), any(Errors.class));
    }
}
