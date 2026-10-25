package org.desempleadosanonimos.backend.application.port;

import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;

public interface FuenteDePublicaciones {

    Flux<Publicacion> publicaciones();
}
