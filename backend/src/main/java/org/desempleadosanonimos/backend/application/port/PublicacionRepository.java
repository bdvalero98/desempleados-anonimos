package org.desempleadosanonimos.backend.application.port;

import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PublicacionRepository {

    Mono<Publicacion> guardar(Publicacion publicacion);

    Flux<Publicacion> listarRecientes();
}
