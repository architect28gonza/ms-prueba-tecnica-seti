package co.com.franquicia.api.dto.request;

import jakarta.validation.constraints.Positive;

public record IdSucursalDTO(
                @Positive(message = "El id de la sucursal no es permitido") Long id) {
}
