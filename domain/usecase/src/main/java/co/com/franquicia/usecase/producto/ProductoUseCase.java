package co.com.franquicia.usecase.producto;

import co.com.franquicia.model.producto.Producto;
import co.com.franquicia.model.producto.gateways.ProductoRepository;
import co.com.franquicia.model.sucursal.gateways.SucursalRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    /**
     * Guarda un producto asociado a una sucursal existente.
     *
     * @param producto el producto a guardar
     * @return un Mono con el producto guardado o error si la sucursal no existe
     */
    public Mono<Producto> save(Producto producto) {
        return sucursalRepository.findByIdSucursal(producto.getSucursal().getId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No existe el sucursal")))
                .flatMap(sucursal -> {
                    producto.setSucursal(sucursal);
                    return productoRepository.saveProducto(producto)
                            .map(saved -> {
                                saved.setSucursal(sucursal);
                                return saved;
                            });
                });
    }

    /**
     * Elimina un producto por su id.
     *
     * @param id el identificador del producto a eliminar
     * @return un Mono vacío o error si el producto no existe
     */
    public Mono<Void> delete(Long id) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto no existe")))
                .flatMap(producto -> productoRepository.deleteProsducto(producto.getId()));
    }

    /**
     * Actualiza el stock de un producto existente.
     *
     * @param id    el identificador del producto
     * @param stock la nueva cantidad de stock
     * @return un Mono con el producto actualizado o error si no existe
     */
    public Mono<Producto> update(Long id, Integer stock) {
        return productoRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto no existe")))
                .flatMap(producto -> productoRepository.updateProducto(id, stock));
    }

    /**
     * Obtiene los productos con el stock máximo de cada sucursal de una franquicia.
     * Cada producto incluirá la información completa de su sucursal.
     *
     * @param idFranquicia ID de la franquicia
     * @return flujo de productos con stock máximo y sucursal completa
     */
    public Flux<Producto> listStockMax(Long idFranquicia) {
        return productoRepository.findProductosConMaxStockPorFranquicia(idFranquicia)
                .flatMap(producto -> sucursalRepository.findByIdSucursal(producto.getSucursal().getId())
                        .map(sucursal -> {
                            producto.setSucursal(sucursal);
                            return producto;
                        }));
    }

}
