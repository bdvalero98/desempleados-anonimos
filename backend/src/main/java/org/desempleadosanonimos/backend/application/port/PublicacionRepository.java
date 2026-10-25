package org.desempleadosanonimos.backend.application.port;

import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.UnaryOperator;

public interface PublicacionRepository {

    Mono<Publicacion> guardar(Publicacion publicacion);

    Flux<Publicacion> listarRecientes();

    Mono<Publicacion> modificar(UUID id, UnaryOperator<Publicacion> cambio);
}
