package co.com.franquicia.api.dto.request;

import jakarta.validation.constraints.Positive;

public record IdFranquiciaDTO(
                @Positive(message = "El id de la franquicia no es permitido") Long id) {
}