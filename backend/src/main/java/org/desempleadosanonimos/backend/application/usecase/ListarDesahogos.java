package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;

public class ListarDesahogos {

    private final PublicacionRepository publicacionRepository;

    public ListarDesahogos(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    public Flux<Publicacion> ejecutar() {
        return publicacionRepository.listarRecientes();
    }
}
