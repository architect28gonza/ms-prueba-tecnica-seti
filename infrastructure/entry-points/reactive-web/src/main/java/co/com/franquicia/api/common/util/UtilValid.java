package co.com.franquicia.api.common.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UtilValid {

    /**
     * Valida el nombre de la franquicia.
     * 
     * @param nombre valor a validar
     * @return mensaje de error si no es válido, null si es correcto
     */
    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre de la franquicia no puede estar vacío o nulo";
        }
        if (!nombre.matches("^[a-zA-Z0-9]+$")) {
            return "El nombre de la franquicia solo puede contener letras y números";
        }
        return null;
    }

    public static String validarId(Long id) {
        if (!(id >= 0)) {
            return "El id del producto no es permitido";
        }
        return null;
    }


}
