package org.desempleadosanonimos.backend.aplication.usecase;

import org.desempleadosanonimos.backend.aplication.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.aplication.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Mono;

public class PublicarDeshago {

    private final PublicacionRepository publicacionRepository;

    private final GeneradorDeAlias generadorDeAlias;

    public PublicarDeshago(PublicacionRepository publicacionRepository,
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
