package co.com.franquicia.usecase.franquicia;

import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.model.franquicia.gateways.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class FranquiciaUseCase {

    private final FranquiciaRepository franquiciaRepository;

    /**
     * Guarda una nueva franquicia si no existe otra con el mismo nombre.
     *
     * @param nombre el nombre de la franquicia a guardar
     * @return un Mono con la franquicia creada o error si ya existe
     */
    public Mono<Franquicia> save(String nombre) {
        return franquiciaRepository.existsByNombre(nombre)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("La franquicia ya existe"));
                    }
                    return franquiciaRepository.saveFranquicia(nombre);
                });
    }

    /**
     * Actualiza el nombre de una franquicia existente.
     *
     * @param id     el identificador de la franquicia a actualizar
     * @param nombre el nuevo nombre a asignar
     * @return un Mono con la franquicia actualizada o error si no existe o el
     *         nombre ya está registrado
     */
    public Mono<Franquicia> updateName(Long id, String nombre) {
        var franquiciaMono = franquiciaRepository.findByIdFranquicia(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe la franquicia con id enviado")));

        Mono<Boolean> nombreExistenteMono = franquiciaRepository.existsByNombre(nombre);
        return Mono.zip(franquiciaMono, nombreExistenteMono).flatMap(tuple -> updateNombre(tuple, id, nombre));
    }

    /**
     * Lógica interna para actualizar el nombre de una franquicia.
     *
     * @param tuple  contiene la franquicia y si el nombre ya existe
     * @param id     el identificador de la franquicia a actualizar
     * @param nombre el nuevo nombre a asignar
     * @return un Mono con la franquicia actualizada o error si el nombre ya está
     *         registrado
     */
    private Mono<Franquicia> updateNombre(
            Tuple2<Franquicia, Boolean> tuple,
            Long id, String nombre) {
        Boolean nombreExistente = tuple.getT2();

        if (nombreExistente) {
            return Mono.error(
                    new IllegalArgumentException("El nombre de la franquicia ya se encuentra registrado"));
        }

        return franquiciaRepository.updateNombreFranquicia(id, nombre);
    }
}