package co.com.franquicia.usecase.sucursal;

import co.com.franquicia.model.franquicia.gateways.FranquiciaRepository;
import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.model.sucursal.gateways.SucursalRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SucursalUseCase {

    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    /**
     * Guarda una sucursal asociada a una franquicia existente.
     *
     * @param sucursal la sucursal a guardar
     * @return un Mono con la sucursal guardada o error si la franquicia no existe
     */
    public Mono<Sucursal> save(Sucursal sucursal) {
        return franquiciaRepository.findByIdFranquicia(sucursal.getFranquicia().getId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe la franquicia")))
                .flatMap(franquicia -> {
                    sucursal.setFranquicia(franquicia);
                    return sucursalRepository.saveSucursal(sucursal)
                            .map(saved -> {
                                saved.setFranquicia(franquicia);
                                return saved;
                            });
                });
    }

    /**
     * Actualiza el nombre de una sucursal existente.
     *
     * @param sucursalId  el identificador de la sucursal a actualizar
     * @param nuevoNombre el nuevo nombre a asignar
     * @return un Mono con la sucursal actualizada o error si no existe o el nombre
     *         ya está registrado
     */
    public Mono<Sucursal> updateNombre(Long sucursalId, String nuevoNombre) {
        return sucursalRepository.findByIdSucursal(sucursalId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe la sucursal con id")))
                .flatMap(sucursal -> sucursalRepository
                        .updateNombreSucursal(sucursalId, nuevoNombre)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException(
                                "No se pudo actualizar. Ya existe otra sucursal con ese nombre en la franquicia.")))
                        .map(sucursalActualizada -> {
                            sucursalActualizada.setFranquicia(sucursal.getFranquicia());
                            return sucursalActualizada;
                        }));
    }

}
