package co.com.franquicia.api.common.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Message {

    public static final String MESSAGE_HANDLER_OK_FRANQUICIA = "La franquicia fue agregado con exito";
    public static final String MESSAGE_HANDLER_UPDATE_NAME_FRANQUICIA = "El nombre de la franquicia ha sido actualizado con exito";
    public static final String MESSAGE_HANDLER_FRANQUICIA = "Entrada al endpoint para agregar franquicias";
    public static final String MESSAGE_ERROR_FRANQUICIA = "Ocurrio un error al momento de agregar una franquicia";
    public static final String MESSAGE_ERROR_FRANQUICIA_EMPTY = "Verifique el nombre de la franquicia para realizar el registro";

    public static final String MESSAGE_HANDLER_OK_SUCURSAL = "La sucursal fue agregado con exito";
    public static final String MESSAGE_HANDLER_UPDATE_NAME_SUCURSAL = "El nombre de la sucursal ha sido actualizado con exito";
    

    public static final String MESSAGE_HANDLER_PRODUCTO = "Producto agregado con exito";
    public static final String MESSAGE_HANDLER_UPDATE_STOCK_PRODUCTO = "El stock del producto ha sido actualizado con exito";
    public static final String MESSAGE_HANDLER_DELETE_PRODUCTO = "Producto eliminido con exito";
    public static final String MESSAGE_HANDLER_LIST_STOCK_MAX = "Lista de productos";

}
