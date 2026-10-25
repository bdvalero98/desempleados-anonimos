package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.FuenteDePublicaciones;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;

public class ObservarDesahogos {

    private final FuenteDePublicaciones fuenteDePublicaciones;

    public ObservarDesahogos(FuenteDePublicaciones fuenteDePublicaciones) {
        this.fuenteDePublicaciones = fuenteDePublicaciones;
    }

    public Flux<Publicacion> ejecutar() {
        return fuenteDePublicaciones.publicaciones();
    }
}
