package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.application.port.NotificadorDePublicaciones;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Mono;

public class PublicarDesahogo {

    private final PublicacionRepository publicacionRepository;
    private final GeneradorDeAlias generadorDeAlias;
    private final NotificadorDePublicaciones notificadorDePublicaciones;

    public PublicarDesahogo(PublicacionRepository publicacionRepository, GeneradorDeAlias generadorDeAlias,
                            NotificadorDePublicaciones notificadorDePublicaciones) {
        this.publicacionRepository = publicacionRepository;
        this.generadorDeAlias = generadorDeAlias;
        this.notificadorDePublicaciones = notificadorDePublicaciones;
    }

    public Mono<Publicacion> ejecutar(String mensaje, Integer diasDesempleado) {
        return Mono.fromSupplier(
                        () -> Publicacion.crear(
                                generadorDeAlias.generar(),
                                mensaje,
                                diasDesempleado
                        )
                )
                .flatMap(publicacionRepository::guardar)
                .flatMap(guardada -> notificadorDePublicaciones.notificar(guardada).thenReturn(guardada));
    }
}
