package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.NotificadorDePublicaciones;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.exception.PublicacionNoEncontradaException;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import org.desempleadosanonimos.backend.domain.model.TipoReaccion;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class ReaccionarADesahogo {

    private final PublicacionRepository publicacionRepository;
    private final NotificadorDePublicaciones notificadorDePublicaciones;

    public ReaccionarADesahogo(PublicacionRepository publicacionRepository,
                               NotificadorDePublicaciones notificadorDePublicaciones) {
        this.publicacionRepository = publicacionRepository;
        this.notificadorDePublicaciones = notificadorDePublicaciones;
    }

    public Mono<Publicacion> ejecutar(UUID idPublicacion, TipoReaccion tipoReaccion) {
        return publicacionRepository.modificar(idPublicacion,
                        publicacion -> publicacion.reaccionar(tipoReaccion))
                .switchIfEmpty(Mono.error(new PublicacionNoEncontradaException(idPublicacion)))
                .flatMap(
                        actualizada -> notificadorDePublicaciones.notificar(actualizada)
                                .thenReturn(actualizada));
    }
}
