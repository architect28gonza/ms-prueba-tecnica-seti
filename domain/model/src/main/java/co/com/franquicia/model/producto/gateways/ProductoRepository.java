package co.com.franquicia.model.producto.gateways;

import co.com.franquicia.model.producto.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepository {

    Mono<Producto> saveProducto(Producto producto);

    Mono<Void> deleteProsducto(Long id);

    Mono<Producto> findById(Long id);

    Mono<Producto> updateProducto(Long id, Integer stock);

    Flux<Producto> findProductosConMaxStockPorFranquicia(Long franquiciaId);
}
