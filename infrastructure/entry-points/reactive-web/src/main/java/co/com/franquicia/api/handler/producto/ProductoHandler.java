package co.com.franquicia.api.handler.producto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.franquicia.api.common.constants.Message;
import co.com.franquicia.api.common.util.ResponseBuilder;
import co.com.franquicia.api.common.util.UtilValid;
import co.com.franquicia.api.dto.request.producto.ProductoDTO;
import co.com.franquicia.api.dto.request.producto.UpdateStockDTO;
import co.com.franquicia.api.validator.ProductoValidator;
import co.com.franquicia.api.validator.Validator;
import co.com.franquicia.usecase.producto.ProductoUseCase;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductoHandler extends ResponseBuilder implements ProductoHandlerContract {

    private final ProductoUseCase productoUseCase;
    private final ProductoValidator validatorProducto;
    private final Validator validator;

    /**
     * Agrega un nuevo producto.
     * 
     * @param serverRequest petición entrante con el body del producto
     * @return respuesta HTTP con el resultado de la creación
     */
    @Override
    public Mono<ServerResponse> add(ServerRequest serverRequest) {
        log.info("Registro de un producto");
        return serverRequest.bodyToMono(ProductoDTO.class)
                .flatMap(validatorProducto::validate)
                .flatMap(productoUseCase::save)
                .flatMap(res -> ResponseBuilder.success(res, Message.MESSAGE_HANDLER_PRODUCTO));
    }

    /**
     * Elimina un producto por id.
     *
     * @param serverRequest petición entrante con el id del producto en la ruta
     * @return respuesta HTTP con el resultado de la eliminación
     */
    @Override
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        log.info("Eliminación de un producto");
        return Mono.just(serverRequest.pathVariable("id_producto"))
                .map(Long::parseLong)
                .flatMap(data -> handleParamm(data, true));
    }

    /**
     * Actualiza el stock de un producto según el id
     *
     * @param serverRequest petición entrante con el id del producto en la ruta
     * @return respuesta HTTP con el resultado de la eliminación
     */
    @Override
    public Mono<ServerResponse> updateStock(ServerRequest serverRequest) {
        log.info("Actualizacion del stock de un producto");
        return serverRequest.bodyToMono(UpdateStockDTO.class)
                .flatMap(validator::validateDto)
                .flatMap(prod -> productoUseCase.update(prod.id(), prod.stock()))
                .flatMap(res -> ResponseBuilder.success(res, Message.MESSAGE_HANDLER_UPDATE_STOCK_PRODUCTO));
    }

    @Override
    public Mono<ServerResponse> list(ServerRequest serverRequest) {
        log.info("Listando productos por franquicia");
        return Mono.just(serverRequest.pathVariable("id_franquicia"))
                .map(Long::parseLong)
                .flatMap(idFranquicia -> handleParamm(idFranquicia, false));
    }

    /**
     * Gestiona la eliminación de un producto validando primero el id.
     * Retorna error si el id no es válido.
     *
     * @param id identificador del producto a eliminar
     * @return respuesta HTTP exitosa o de error según la validación
     */
    private Mono<ServerResponse> handleParamm(Long id, boolean isDelete) {
        String errorMsg = UtilValid.validarId(id);
        System.out.println("Entrada::: " + id);

        if (errorMsg != null) {
            log.error("El Id del producto no existe");
            return error(errorMsg, HttpStatus.BAD_REQUEST.value());
        }

        return isDelete ? deleteProducto(id)
                .then(success(null, Message.MESSAGE_HANDLER_DELETE_PRODUCTO))
                : productoUseCase.listStockMax(id)
                        .collectList()
                        .flatMap(data -> success(data, Message.MESSAGE_HANDLER_LIST_STOCK_MAX));
    }

    /**
     * Ejecuta la eliminación del producto a través del caso de uso.
     *
     * @param id identificador del producto a eliminar
     * @return operación reactiva sin respuesta (void)
     */
    private Mono<Void> deleteProducto(Long id) {
        log.info("Eliminación de un producto");
        return productoUseCase.delete(id);
    }
}
