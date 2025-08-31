package co.com.franquicia.api.dto.request.sucursal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UpdateNameDTO(

        @Positive(message = "El id de la sucursal no es permitido") 
        @NotNull(message = "El id para actualizar la sucursal no es permitido") 
        Long id,

        @NotNull(message = "El nombre de la sucursal no es permitido") 
        @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre de la sucursal solo puede contener letras, números y espacios")
        String nombre
) {}
