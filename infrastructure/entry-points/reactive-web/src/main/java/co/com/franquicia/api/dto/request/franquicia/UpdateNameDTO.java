package co.com.franquicia.api.dto.request.franquicia;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UpdateNameDTO(

        @Positive(message = "El id de la franquicia no es permitido") 
        @NotNull(message = "El id para actualizar la franquicia no es permitido no es valido") 
        Long id,

        @NotNull(message = "El nombre de la franquicia no es permitido") 
        @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "El nombre de la franquicia solo puede contener letras, números y espacios")
        String nombre
) {}
