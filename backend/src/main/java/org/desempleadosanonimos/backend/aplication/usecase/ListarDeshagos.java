package org.desempleadosanonimos.backend.aplication.usecase;

import org.desempleadosanonimos.backend.aplication.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import reactor.core.publisher.Flux;

public class ListarDeshagos {

    private final PublicacionRepository publicacionRepository;

    public ListarDeshagos(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    public Flux<Publicacion> ejecutar() {
        return publicacionRepository.listarRecientes();
    }
}
