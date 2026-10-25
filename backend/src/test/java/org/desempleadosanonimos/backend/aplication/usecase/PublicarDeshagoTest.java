package org.desempleadosanonimos.backend.aplication.usecase;

import org.desempleadosanonimos.backend.aplication.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.aplication.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.exception.PublicacionInvalidaException;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

class PublicarDeshagoTest {

    private final List<Publicacion> guardadas = new ArrayList<>();

    private final PublicacionRepository repositorioFalso = new PublicacionRepository() {
        @Override
        public Mono<Publicacion> guardar(Publicacion publicacion) {
            guardadas.add(publicacion);
            return Mono.just(publicacion);
        }

        @Override
        public Flux<Publicacion> listarRecientes() {
            return Flux.fromIterable(guardadas);
        }
    };

    private final GeneradorDeAlias aliasFijo = () -> "Alias de Prueba";

    private final PublicarDeshago publicarDeshago = new PublicarDeshago(repositorioFalso, aliasFijo);

    @Test
    void publicaYGuardaElDesahogo() {
        StepVerifier.create(
                        publicarDeshago.ejecutar("Dia largo", 15)
                )
                .expectNextMatches(p -> p.alias().equals("Alias de Prueba")
                        && p.mensaje().equals("Dia largo"))
                .verifyComplete();
    }

    @Test
    void fallaSiElMensajeEstaVacio() {
        StepVerifier.create(
                        publicarDeshago.ejecutar(" ", 15)
                )
                .expectError(PublicacionInvalidaException.class)
                .verify();
    }
}