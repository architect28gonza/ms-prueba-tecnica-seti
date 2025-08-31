package co.com.franquicia.api.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

import co.com.franquicia.api.dto.request.producto.ProductoDTO;
import co.com.franquicia.api.exceptions.ApiException;
import co.com.franquicia.model.producto.Producto;
import co.com.franquicia.model.sucursal.Sucursal;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductoValidator {

    private final SmartValidator validator;

    /**
     * Valida el DTO de un producto y lo convierte en una entidad Prodcuto.
     * Lanza ApiException si hay errores de validación.
     *
     * @param request DTO de producto
     * @return Mono con la entidad Producto válida
     */
    public Mono<Producto> validate(ProductoDTO request) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, ProductoDTO.class.getName());
        validator.validate(request, errors);

        if (errors.hasErrors()) {
            String msg = errors.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Error de validación");
            return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, msg));
        }

        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setStock(request.stock());

        if (request.sucursal() != null && request.sucursal().id() != null) {
            Sucursal sucursal = new Sucursal();
            sucursal.setId(request.sucursal().id());
            producto.setSucursal(sucursal);
        }

        return Mono.just(producto);
    }
}
