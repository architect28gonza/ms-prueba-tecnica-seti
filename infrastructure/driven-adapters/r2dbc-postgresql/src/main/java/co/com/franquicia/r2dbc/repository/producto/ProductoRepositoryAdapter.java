package co.com.franquicia.r2dbc.repository.producto;

import co.com.franquicia.model.producto.Producto;
import co.com.franquicia.model.producto.gateways.ProductoRepository;
import co.com.franquicia.model.sucursal.Sucursal;
import co.com.franquicia.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoRepositoryAdapter
        extends ReactiveAdapterOperations<Producto, ProductoEntity, Long, ProductoReactiveRepository>
        implements ProductoRepository {

    public ProductoRepositoryAdapter(ProductoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Producto.class));
    }

    /**
     * Guarda un producto con sus datos y sucursal asociada.
     *
     * @param producto el producto a guardar
     * @return un Mono con el producto guardado
     */
    @Override
    public Mono<Producto> saveProducto(Producto producto) {
        return this.repository.save(producto.getNombre(),
                producto.getStock(), producto.getSucursal().getId())
                .map(this::toEntity);
    }

    /**
     * Elimina un producto por su identificador.
     *
     * @param id el identificador del producto a eliminar
     * @return un Mono vacío
     */
    @Override
    public Mono<Void> deleteProsducto(Long id) {
        return this.repository.deleteById(id);
    }

    /**
     * Obtiene un producto por su identificador.
     *
     * @param id el identificador del producto
     * @return un Mono con el producto encontrado o vacío si no existe
     */
    @Override
    public Mono<Producto> findById(Long id) {
        return this.repository.findById(id).map(this::toEntity);
    }

    /**
     * Actualiza el stock de un producto existente.
     *
     * @param id    el identificador del producto
     * @param stock la nueva cantidad de stock
     * @return un Mono con el producto actualizado
     */
    @Override
    public Mono<Producto> updateProducto(Long id, Integer stock) {
        return this.repository.updateStock(id, stock).map(this::toEntity);
    }

    /**
     * Obtiene los productos con stock máximo por franquicia, asignando solo
     * la información básica de la sucursal (ID).
     *
     * @param franquiciaId ID de la franquicia
     * @return flujo de productos con stock máximo y sucursal con ID
     */
    public Flux<Producto> findProductosConMaxStockPorFranquicia(Long franquiciaId) {
        return repository.findProductosConMaxStockPorFranquicia(franquiciaId)
                .map(this::mapToProducto);
    }

    /**
     * Convierte un registro de datos en un objeto Producto.
     *
     * @param data registro de datos de producto
     * @return objeto Producto
     */
    private Producto mapToProducto(ProductoEntity data) {
        Producto producto = new Producto();
        producto.setId(data.getId());
        producto.setNombre(data.getNombre());
        producto.setStock(data.getStock());

        Sucursal sucursal = new Sucursal();
        sucursal.setId(data.getId());
        producto.setSucursal(sucursal);

        return producto;
    }
}
