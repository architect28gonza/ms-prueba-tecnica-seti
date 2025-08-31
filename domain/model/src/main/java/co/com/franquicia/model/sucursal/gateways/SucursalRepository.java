package co.com.franquicia.model.sucursal.gateways;

import java.util.List;

import co.com.franquicia.model.sucursal.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepository {

    Mono<Sucursal> saveSucursal(Sucursal sucursal);

    Mono<Sucursal> findByIdSucursal(Long id);

    Mono<Sucursal> updateNombreSucursal(Long id, String nombre);

    Flux<Sucursal> findAllById(List<Long> ids);
}
