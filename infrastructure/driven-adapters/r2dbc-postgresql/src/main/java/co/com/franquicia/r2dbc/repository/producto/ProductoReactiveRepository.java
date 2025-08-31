package co.com.franquicia.r2dbc.repository.producto;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoReactiveRepository
		extends ReactiveCrudRepository<ProductoEntity, Long>, ReactiveQueryByExampleExecutor<ProductoEntity> {

	@Query("INSERT INTO tbl_producto (nombre, stock, id_sucursal) VALUES (:nombre, :stock, :id) RETURNING *")
	Mono<ProductoEntity> save(
			@Param("nombre") String nombre,
			@Param("stock") Integer stock,
			@Param("id") Long id);

	@Query("UPDATE tbl_producto SET stock = :stock WHERE id = :id RETURNING *")
	Mono<ProductoEntity> updateStock(
			@Param("id") Long id,
			@Param("stock") Integer stock);

	@Query("""
			    SELECT p.*
			    FROM tbl_producto p
			    JOIN tbl_sucursal s ON p.id_sucursal = s.id
			    WHERE s.id_franquicia = :idFranquicia
			      AND p.stock = (
			          SELECT MAX(p2.stock)
			          FROM tbl_producto p2
			          WHERE p2.id_sucursal = p.id_sucursal
			      )
			""")
	Flux<ProductoEntity> findProductosConMaxStockPorFranquicia(@Param("idFranquicia") Long idFranquicia);

}
