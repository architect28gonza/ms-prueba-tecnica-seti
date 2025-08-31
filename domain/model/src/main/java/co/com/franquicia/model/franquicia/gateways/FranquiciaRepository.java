package co.com.franquicia.model.franquicia.gateways;

import co.com.franquicia.model.franquicia.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {

    Mono<Franquicia> saveFranquicia(String nombre);

    Mono<Boolean> existsByNombre(String nombre);

    Mono<Franquicia> findByIdFranquicia(Long id);

    Mono<Franquicia> updateNombreFranquicia(Long id, String nombre);
}
