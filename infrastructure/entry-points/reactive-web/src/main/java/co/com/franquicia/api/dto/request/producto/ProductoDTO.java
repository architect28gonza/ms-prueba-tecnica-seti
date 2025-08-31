package co.com.franquicia.api.dto.request.producto;

import co.com.franquicia.api.dto.request.IdSucursalDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductoDTO(
        
        @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre del producto solo puede contener letras, números y espacios")
        @Size(min = 7, max = 50, message = "El nombre del producto debe tener entre 7 y 50 caracteres") 
        @NotBlank(message = "El nombre no puede estar vacío") 
        String nombre,

        @NotNull(message = "El stock del producto no puede ser nulo")
        @Positive(message = "El stock debe ser mayor que cero")
        Integer stock,
        
        @NotNull(message = "La sucursal no puede ser nulo")
        IdSucursalDTO sucursal) {
}
