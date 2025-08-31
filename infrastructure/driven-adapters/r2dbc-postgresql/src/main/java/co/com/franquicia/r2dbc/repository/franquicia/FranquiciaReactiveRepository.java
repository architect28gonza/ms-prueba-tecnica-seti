package co.com.franquicia.r2dbc.repository.franquicia;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaReactiveRepository
                extends ReactiveCrudRepository<FranquiciaEntity, Long>,
                ReactiveQueryByExampleExecutor<FranquiciaEntity> {

        @Query("INSERT INTO tbl_franquicia (nombre) VALUES (:nombre) RETURNING *")
        Mono<FranquiciaEntity> save(@Param("nombre") String nombre);

        @Query("UPDATE tbl_franquicia SET nombre = :nombre WHERE id = :id RETURNING *")
        Mono<FranquiciaEntity> updateNombre(
                        @Param("id") Long id,
                        @Param("nombre") String nombre);

        Mono<Boolean> existsByNombre(String nombre);
}
