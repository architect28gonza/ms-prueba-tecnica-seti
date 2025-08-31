package co.com.franquicia.api.dto.request.sucursal;

import co.com.franquicia.api.dto.request.IdFranquiciaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SucursalDTO(
        
        @Size(min = 7, max = 50, message = "El nombre debe tener entre 7 y 50 caracteres")
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotNull(message = "La franquicia no puede estar vacia")
        IdFranquiciaDTO franquicia) {
}
