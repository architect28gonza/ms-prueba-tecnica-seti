package co.com.franquicia.api.handler.franquicia;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Message;
import co.com.franquicia.api.common.util.ResponseBuilder;
import co.com.franquicia.api.common.util.UtilValid;
import co.com.franquicia.api.dto.request.franquicia.UpdateNameDTO;
import co.com.franquicia.api.validator.Validator;
import co.com.franquicia.usecase.franquicia.FranquiciaUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranquiciaHandler extends ResponseBuilder implements FranquiciaHandlerContract {

	private final FranquiciaUseCase franquiciaUseCase;
	private final Validator validator;

	/**
	 * Agrega una nueva franquicia a partir del path variable recibido.
	 *
	 * @param serverRequest petición entrante con el nombre de la franquicia en la
	 *                      ruta
	 * @return respuesta HTTP con el resultado de la creación
	 */
	@Override
	public Mono<ServerResponse> add(ServerRequest serverRequest) {
		log.info("Registro de una franquicia");
		return Mono.fromSupplier(() -> serverRequest.pathVariable("nombre_franquicia"))
				.flatMap(this::handleAddFranquicia);
	}

	/**
	 * Actualiza el nombre de una franquicia desde la solicitud HTTP.
	 *
	 * @param serverRequest la solicitud del cliente con los datos de actualización
	 * @return un Mono con la respuesta HTTP indicando éxito o error
	 */
	@Override
	public Mono<ServerResponse> updateName(ServerRequest serverRequest) {
		log.info("Actualizacion del nombre de una franquicia");
		return serverRequest.bodyToMono(UpdateNameDTO.class)
				.flatMap(dto -> validator.validateDto(dto))
				.flatMap(fra -> franquiciaUseCase.updateName(fra.id(), fra.nombre()))
				.flatMap(res -> ResponseBuilder.success(res, Message.MESSAGE_HANDLER_UPDATE_NAME_FRANQUICIA));
	}

	/**
	 * Valida el nombre de la franquicia y gestiona el flujo de creación.
	 * Retorna error si el nombre no es válido.
	 *
	 * @param nombreFranquicia nombre de la franquicia a crear
	 * @return respuesta HTTP exitosa o de error según la validación
	 */
	private Mono<ServerResponse> handleAddFranquicia(String nombreFranquicia) {
		String errorMsg = UtilValid.validarNombre(nombreFranquicia);

		if (errorMsg != null) {
			log.error(Message.MESSAGE_ERROR_FRANQUICIA_EMPTY);
			return error(errorMsg, HttpStatus.BAD_REQUEST.value());
		}

		return saveFranquicia(nombreFranquicia);
	}

	/**
	 * Ejecuta la creación de la franquicia mediante el caso de uso.
	 * Maneja la respuesta exitosa o el error en la persistencia.
	 *
	 * @param nombreFranquicia nombre de la franquicia a guardar
	 * @return respuesta HTTP exitosa o de error
	 */
	private Mono<ServerResponse> saveFranquicia(String nombreFranquicia) {
		log.info(Message.MESSAGE_HANDLER_FRANQUICIA);

		return franquiciaUseCase.save(nombreFranquicia)
				.flatMap(res -> FranquiciaHandler.success(res, Message.MESSAGE_HANDLER_OK_FRANQUICIA))
				.onErrorResume(ex -> handleSaveError(ex, nombreFranquicia));
	}

	/**
	 * Maneja los errores ocurridos durante la creación de una franquicia.
	 * Diferencia entre error por duplicidad y error interno.
	 *
	 * @param ex               excepción lanzada
	 * @param nombreFranquicia nombre de la franquicia asociada al error
	 * @return respuesta HTTP con el mensaje y código de error apropiado
	 */
	private Mono<ServerResponse> handleSaveError(Throwable ex, String nombreFranquicia) {
		if (ex instanceof IllegalArgumentException && ex.getMessage().contains("ya existe")) {
			log.warn("Intento de crear franquicia duplicada: {}", nombreFranquicia);
			return error("La franquicia ya existe", HttpStatus.CONFLICT.value());
		}

		log.error(Message.MESSAGE_ERROR_FRANQUICIA, ex);
		return error(Message.MESSAGE_ERROR_FRANQUICIA, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
