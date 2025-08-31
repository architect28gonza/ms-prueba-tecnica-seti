package co.com.franquicia.api.validator;

import co.com.franquicia.api.exceptions.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidatorTest {

    private SmartValidator smartValidator;
    private Validator validator;

    @BeforeEach
    void setUp() {
        smartValidator = mock(SmartValidator.class);
        validator = new Validator(smartValidator);
    }

    @Test
    void testValidateDto_validObject() {
        TestDto dto = new TestDto("valid");

        doAnswer(invocation -> null).when(smartValidator).validate(eq(dto), any(Errors.class));

        StepVerifier.create(validator.validateDto(dto))
                .expectNext(dto)
                .verifyComplete();

        verify(smartValidator).validate(eq(dto), any(Errors.class));
    }

    @Test
    void testValidateDto_invalidObject() {
        TestDto dto = new TestDto("invalid");

        // Simulamos errores
        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.reject("400", "Nombre inválido");
            return null;
        }).when(smartValidator).validate(eq(dto), any(Errors.class));

        StepVerifier.create(validator.validateDto(dto))
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof ApiException);
                    ApiException ex = (ApiException) throwable;
                    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
                    assertTrue(ex.getMessage().contains("Nombre inválido"));
                })
                .verify();

        verify(smartValidator).validate(eq(dto), any(Errors.class));
    }

    // DTO de prueba
    record TestDto(String nombre) {
    }
}