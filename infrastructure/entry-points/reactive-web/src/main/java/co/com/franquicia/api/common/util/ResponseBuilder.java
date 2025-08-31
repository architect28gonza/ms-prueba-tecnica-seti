package co.com.franquicia.api.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.dto.response.ApiResponse;
import reactor.core.publisher.Mono;

public abstract class ResponseBuilder {

	/**
	 * Construye una respuesta HTTP 200 OK con un cuerpo de datos.
	 *
	 * @param <T>  tipo del cuerpo de la respuesta
	 * @param body contenido de la respuesta
	 * @return Mono con ServerResponse listo para enviar al cliente
	 */
	public static <T> Mono<ServerResponse> success(T body, String msg) {
		ApiResponse<T> response = new ApiResponse<>(
				new ApiResponse.Header(msg, HttpStatus.OK.value()),
				body);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(response);
	}

	/**
	 * Construye una respuesta HTTP con un código de error y mensaje personalizado.
	 *
	 * @param <T>     tipo del cuerpo de la respuesta (usualmente null)
	 * @param message mensaje de error
	 * @param code    código HTTP de la respuesta
	 * @return Mono con ServerResponse de error listo para enviar al cliente
	 */
	public static <T> Mono<ServerResponse> error(String message, Integer code) {
		ApiResponse<T> response = new ApiResponse<>(
				new ApiResponse.Header(message, code), null);
		return ServerResponse.status(code)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(response);
	}
}
