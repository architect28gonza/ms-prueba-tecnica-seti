package co.com.franquicia.api.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

import co.com.franquicia.api.dto.request.sucursal.SucursalDTO;
import co.com.franquicia.api.exceptions.ApiException;
import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.model.sucursal.Sucursal;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SucursalValidator {

    private final SmartValidator validator;

    /**
     * Valida el DTO de sucursal y lo convierte en una entidad Sucursal.
     * Lanza ApiException si hay errores de validación.
     *
     * @param request DTO de sucursal
     * @return Mono con la entidad Sucursal válida
     */
    public Mono<Sucursal> validate(SucursalDTO request) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, SucursalDTO.class.getName());
        validator.validate(request, errors);

        if (errors.hasErrors()) {
            String msg = errors.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Error de validación");
            return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, msg));
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(request.nombre());

        if (request.franquicia() != null && request.franquicia().id() != null) {
            Franquicia franquicia = new Franquicia();
            franquicia.setId(request.franquicia().id());
            sucursal.setFranquicia(franquicia);
        }

        return Mono.just(sucursal);
    }
}
