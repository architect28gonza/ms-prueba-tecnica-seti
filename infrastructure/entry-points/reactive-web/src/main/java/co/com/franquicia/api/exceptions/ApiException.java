package co.com.franquicia.api.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Constructor de ApiException.
     *
     * @param status  el código HTTP que representa el error
     * @param message el mensaje de la excepción
     */
    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Obtiene el código HTTP asociado a la excepción.
     *
     * @return el código HTTP
     */
    public HttpStatus getStatus() {
        return status;
    }
}
