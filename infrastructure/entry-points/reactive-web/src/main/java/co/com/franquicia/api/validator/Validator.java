package co.com.franquicia.api.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

import co.com.franquicia.api.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Validator {

    private final SmartValidator validator;

    public <T> Mono<T> validateDto(T request) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, request.getClass().getName());
        validator.validate(request, errors);

        if (errors.hasErrors()) {
            String msg = errors.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Error de validación");
            return Mono.error(new ApiException(HttpStatus.BAD_REQUEST, msg));
        }

        return Mono.just(request);
    }
}
