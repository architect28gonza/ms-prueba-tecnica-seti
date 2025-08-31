package co.com.franquicia.r2dbc.repository.sucursal;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalReactiveRepository
		extends ReactiveCrudRepository<SucursalEntity, Long>, ReactiveQueryByExampleExecutor<SucursalEntity> {

	@Query("INSERT INTO tbl_sucursal (nombre, id_franquicia) VALUES (:nombre, :id) RETURNING *")
	Mono<SucursalEntity> save(
			@Param("nombre") String nombre,
			@Param("id") Long id);

	@Query("UPDATE tbl_sucursal SET nombre = :nombre WHERE id = :id RETURNING *")
	Mono<SucursalEntity> updateNombre(
			@Param("id") Long id,
			@Param("nombre") String nombre);

	@Query("SELECT * FROM tbl_sucursal WHERE id IN (:ids)")
	Flux<SucursalEntity> findByIds(@Param("ids") List<Long> ids);
}