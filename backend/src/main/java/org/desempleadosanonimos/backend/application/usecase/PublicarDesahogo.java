package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Mono;

public class PublicarDesahogo {

    private final PublicacionRepository publicacionRepository;

    private final GeneradorDeAlias generadorDeAlias;

    public PublicarDesahogo(PublicacionRepository publicacionRepository,
                            GeneradorDeAlias generadorDeAlias) {

        this.publicacionRepository = publicacionRepository;
        this.generadorDeAlias = generadorDeAlias;
    }

    public Mono<Publicacion> ejecutar(String mensaje, Integer diasDesempleado) {

        return Mono.fromSupplier(
                () -> Publicacion.crear(
                        generadorDeAlias.generar(),
                        mensaje,
                        diasDesempleado
                )
        ).flatMap(publicacionRepository::guardar);
    }
}
