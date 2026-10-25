package org.desempleadosanonimos.backend.application.port;

import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Mono;

public interface NotificadorDePublicaciones {

    Mono<Void> notificar(Publicacion publicacion);
}
