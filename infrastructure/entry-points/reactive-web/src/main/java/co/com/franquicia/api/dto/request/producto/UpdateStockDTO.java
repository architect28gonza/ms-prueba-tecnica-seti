package co.com.franquicia.api.dto.request.producto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateStockDTO(
        @Positive(message = "El id del producto no es permitido")
        @NotNull(message = "El id para actualizar el producto no es valido") 
        Long id,

        @NotNull(message = "El stock del producto no es permitido")
        @Positive(message = "El stock debe ser mayor que cero")
        Integer stock) {
}
