package co.com.franquicia.api.handler.sucursal;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Message;
import co.com.franquicia.api.common.util.ResponseBuilder;
import co.com.franquicia.api.dto.request.sucursal.UpdateNameDTO;
import co.com.franquicia.api.dto.request.sucursal.SucursalDTO;
import co.com.franquicia.api.validator.SucursalValidator;
import co.com.franquicia.api.validator.Validator;
import co.com.franquicia.usecase.sucursal.SucursalUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SucursalHandler extends ResponseBuilder implements SucursalHandlerContract {

    private final SucursalUseCase sucursalUseCase;
    private final SucursalValidator validatorSucursal;
    private final Validator validator;

    /**
     * Agrega una nueva sucursal.
     * Valida el DTO recibido, lo convierte a entidad y lo persiste.
     *
     * @param serverRequest la solicitud HTTP entrante con el DTO de la sucursal
     * @return Mono con ServerResponse con la sucursal creada o error
     */
    @Override
    public Mono<ServerResponse> add(ServerRequest serverRequest) {
        log.info("Registro de un sucursal");
        return serverRequest.bodyToMono(SucursalDTO.class)
                .flatMap(validatorSucursal::validate)
                .flatMap(sucursalUseCase::save)
                .flatMap(res -> ResponseBuilder.success(res, Message.MESSAGE_HANDLER_OK_SUCURSAL));
    }

    /**
     * Actualiza el nombre de una sucursal desde la solicitud HTTP.
     *
     * @param serverRequest la solicitud del cliente con los datos de actualización
     * @return un Mono con la respuesta HTTP indicando éxito o error
     */
    @Override
    public Mono<ServerResponse> updateName(ServerRequest serverRequest) {
        log.info("Actualizacion del nombre de una sucursal");
        return serverRequest.bodyToMono(UpdateNameDTO.class)
                .flatMap(dto -> validator.validateDto(dto))
                .flatMap(suc -> sucursalUseCase.updateNombre(suc.id(), suc.nombre()))
                .flatMap(res -> ResponseBuilder.success(res, Message.MESSAGE_HANDLER_UPDATE_NAME_SUCURSAL));
    }
}
