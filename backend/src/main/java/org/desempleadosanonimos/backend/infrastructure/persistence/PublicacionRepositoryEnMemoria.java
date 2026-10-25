package org.desempleadosanonimos.backend.infrastructure.persistence;

import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

public class PublicacionRepositoryEnMemoria implements PublicacionRepository {

    private static final int CANTIDAD_MAXIMA = 50;

    private final Map<UUID, Publicacion> almacen = new ConcurrentHashMap<>();

    @Override
    public Mono<Publicacion> guardar(Publicacion publicacion) {
        return Mono.fromSupplier(
                () -> {
                    almacen.put(publicacion.id(), publicacion);
                    return publicacion;
                }
        );
    }

    @Override
    public Flux<Publicacion> listarRecientes() {
        return Flux.fromStream(
                () -> almacen.values().stream()
                        .sorted(Comparator.comparing(Publicacion::creadaEn).reversed())
                        .limit(CANTIDAD_MAXIMA)
        );
    }

    @Override
    public Mono<Publicacion> modificar(UUID id, UnaryOperator<Publicacion> cambio) {
        return Mono.fromSupplier(
                () -> almacen.computeIfPresent(id, (clave, actual) -> cambio.apply(actual))
        );
    }
}
