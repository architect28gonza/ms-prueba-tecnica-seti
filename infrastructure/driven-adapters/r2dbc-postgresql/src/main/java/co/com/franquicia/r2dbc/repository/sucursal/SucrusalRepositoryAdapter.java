package co.com.franquicia.r2dbc.repository.sucursal;

import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.model.sucursal.gateways.SucursalRepository;
import co.com.franquicia.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SucrusalRepositoryAdapter
        extends ReactiveAdapterOperations<Sucursal, SucursalEntity, Long, SucursalReactiveRepository>
        implements SucursalRepository {

    public SucrusalRepositoryAdapter(SucursalReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Sucursal.class));
    }

    /**
     * Guarda una sucursal asociada a una franquicia.
     *
     * @param sucursal la sucursal a guardar
     * @return un Mono con la sucursal guardada
     */
    @Override
    public Mono<Sucursal> saveSucursal(Sucursal sucursal) {
        return this.repository.save(sucursal.getNombre(), sucursal.getFranquicia().getId())
                .map(this::toEntity);
    }

    /**
     * Obtiene una sucursal por su identificador.
     *
     * @param id el identificador de la sucursal
     * @return un Mono con la sucursal encontrada o vacío si no existe
     */
    @Override
    public Mono<Sucursal> findByIdSucursal(Long id) {
        return this.repository.findById(id).map(this::toEntity);
    }

    /**
     * Actualiza el nombre de una sucursal existente.
     *
     * @param id     el identificador de la sucursal a actualizar
     * @param nombre el nuevo nombre a asignar
     * @return un Mono con la sucursal actualizada
     */
    @Override
    public Mono<Sucursal> updateNombreSucursal(Long id, String nombre) {
        return this.repository.updateNombre(id, nombre).map(this::toEntity);
    }

    @Override
    public Flux<Sucursal> findAllById(List<Long> ids) {
        return this.repository.findByIds(ids).map(this::toEntity);
    }
}
