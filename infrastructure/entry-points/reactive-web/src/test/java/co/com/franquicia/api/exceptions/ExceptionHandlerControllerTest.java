package co.com.franquicia.api.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerControllerTest {

    private final ExceptionHandlerController handler = new ExceptionHandlerController();

    @Test
    void testHandleApiException() {
        ApiException ex = new ApiException(HttpStatus.NOT_FOUND, "Error API");

        Mono<ResponseEntity<Error<Object>>> result = handler.handleApiException(ex);

        ResponseEntity<Error<Object>> response = result.block();
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error API", response.getBody().getHeader().getMessage());
        assertEquals(404, response.getBody().getHeader().getCode());
    }

    @Test
    void testHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        Mono<ResponseEntity<Error<Object>>> result = handler.handleIllegalArgument(ex);

        ResponseEntity<Error<Object>> response = result.block();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argumento inválido", response.getBody().getHeader().getMessage());
        assertEquals(400, response.getBody().getHeader().getCode());
    }
}