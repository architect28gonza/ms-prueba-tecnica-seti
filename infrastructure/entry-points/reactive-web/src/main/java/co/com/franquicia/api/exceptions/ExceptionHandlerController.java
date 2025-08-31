package co.com.franquicia.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Maneja las excepciones personalizadas ApiException.
     * 
     * @param ex la excepción ApiException lanzada
     * @return un Mono que contiene ResponseEntity con el error y el status
     *         correspondiente
     */
    @ExceptionHandler(ApiException.class)
    public Mono<ResponseEntity<Error<Object>>> handleApiException(ApiException ex) {
        return Mono.just(
                ResponseEntity
                        .status(ex.getStatus())
                        .body(buildError(ex.getMessage(), ex.getStatus().value())));
    }

    /**
     * Maneja las excepciones IllegalArgumentException.
     * Se considera como un error de solicitud inválida (400 BAD_REQUEST).
     * 
     * @param ex la excepción IllegalArgumentException lanzada
     * @return un Mono que contiene ResponseEntity con el error y el status 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Error<Object>>> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(buildError(ex.getMessage(), HttpStatus.BAD_REQUEST.value())));
    }

    /**
     * Construye un objeto Error con el mensaje y el código de estado
     * proporcionados.
     * 
     * @param message mensaje de error
     * @param status  código HTTP del error
     * @return objeto Error con el header y body nulo
     */
    private Error<Object> buildError(String message, int status) {
        Error<Object> error = new Error<>();
        Error.Header header = new Error.Header(message, status);
        error.setHeader(header);
        error.setBody(null);
        return error;
    }
}
