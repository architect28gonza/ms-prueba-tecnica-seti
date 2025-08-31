package co.com.franquicia.r2dbc.repository.franquicia;

import co.com.franquicia.model.franquicia.Franquicia;
import co.com.franquicia.model.franquicia.gateways.FranquiciaRepository;
import co.com.franquicia.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FranquiciaRepositoryAdapter
        extends ReactiveAdapterOperations<Franquicia, FranquiciaEntity, Long, FranquiciaReactiveRepository>
        implements FranquiciaRepository {

    public FranquiciaRepositoryAdapter(FranquiciaReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franquicia.class));
    }

    /**
     * Guarda una franquicia con el nombre indicado.
     *
     * @param nombre el nombre de la franquicia a guardar
     * @return un Mono con la franquicia guardada
     */
    @Override
    public Mono<Franquicia> saveFranquicia(String nombre) {
        return this.repository.save(nombre)
                .map(this::toEntity);
    }

    /**
     * Guarda una franquicia con el nombre indicado.
     *
     * @param nombre el nombre de la franquicia a guardar
     * @return un Mono con la franquicia guardada
     */
    @Override
    public Mono<Boolean> existsByNombre(String nombre) {
        return this.repository.existsByNombre(nombre);
    }

    /**
     * Obtiene una franquicia por su identificador.
     *
     * @param id el identificador de la franquicia
     * @return un Mono con la franquicia encontrada o vacío si no existe
     */
    @Override
    public Mono<Franquicia> findByIdFranquicia(Long id) {
        return this.repository.findById(id).map(this::toEntity);
    }

    /**
     * Actualiza el nombre de una franquicia existente.
     *
     * @param id     el identificador de la franquicia a actualizar
     * @param nombre el nuevo nombre a asignar
     * @return un Mono con la franquicia actualizada
     */
    @Override
    public Mono<Franquicia> updateNombreFranquicia(Long id, String nombre) {
        return this.repository.updateNombre(id, nombre).map(this::toEntity);
    }
}
