package co.com.franquicia.api.validator;

import co.com.franquicia.api.dto.request.IdSucursalDTO;
import co.com.franquicia.api.dto.request.producto.ProductoDTO;
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

class ProductoValidatorTest {

    private SmartValidator smartValidator;
    private ProductoValidator productoValidator;

    @BeforeEach
    void setUp() {
        smartValidator = mock(SmartValidator.class);
        // Inyectamos el SmartValidator en la clase a probar
        productoValidator = new ProductoValidator(smartValidator);
    }

    @Test
    void testValidate_validProductoDTO() {
        IdSucursalDTO idSucursalDTO = new IdSucursalDTO(1L);
        ProductoDTO dto = new ProductoDTO("Producto 1", 100, idSucursalDTO);

        doAnswer(invocation -> null)
                .when(smartValidator).validate(eq(dto), any(Errors.class));

        StepVerifier.create(productoValidator.validate(dto))
                .assertNext(producto -> {
                    assertEquals("Producto 1", producto.getNombre());
                    assertEquals(100, producto.getStock());
                    assertNotNull(producto.getSucursal());
                    assertEquals(1L, producto.getSucursal().getId());
                })
                .verifyComplete();

        verify(smartValidator).validate(eq(dto), any(Errors.class));
    }

    @Test
    void testValidate_invalidProductoDTO() {
        ProductoDTO dto = new ProductoDTO("", -1, null);

        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.reject("400", "Nombre inválido");
            errors.reject("400", "Stock inválido");
            return null;
        }).when(smartValidator).validate(eq(dto), any(Errors.class));

        StepVerifier.create(productoValidator.validate(dto))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof ApiException);
                    ApiException ex = (ApiException) error;
                    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
                    assertTrue(ex.getMessage().contains("Nombre inválido"));
                    assertTrue(ex.getMessage().contains("Stock inválido"));
                })
                .verify();

        verify(smartValidator).validate(eq(dto), any(Errors.class));
    }
}