package org.desempleadosanonimos.backend.infrastructure.live;

import org.desempleadosanonimos.backend.application.port.FuenteDePublicaciones;
import org.desempleadosanonimos.backend.application.port.NotificadorDePublicaciones;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class PublicacionesEnVivoConSink implements NotificadorDePublicaciones, FuenteDePublicaciones {

    private final Sinks.Many<Publicacion> compuerta = Sinks.many().multicast().directBestEffort();

    @Override
    public Flux<Publicacion> publicaciones() {
        return compuerta.asFlux();
    }

    @Override
    public Mono<Void> notificar(Publicacion publicacion) {
        return Mono.fromRunnable(
                () -> emitir(publicacion)
        );
    }

    private synchronized void emitir(Publicacion publicacion) {
        compuerta.tryEmitNext(publicacion);
    }
}
