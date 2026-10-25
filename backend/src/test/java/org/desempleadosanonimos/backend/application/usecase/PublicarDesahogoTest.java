package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.application.port.NotificadorDePublicaciones;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.exception.PublicacionInvalidaException;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

class PublicarDesahogoTest {

    private final List<Publicacion> guardadas = new ArrayList<>();
    private final List<Publicacion> notificadas = new ArrayList<>();

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

        @Override
        public Mono<Publicacion> modificar(UUID id, UnaryOperator<Publicacion> cambio) {
            return Mono.empty();
        }
    };

    private final GeneradorDeAlias aliasFijo = () -> "Alias de Prueba";

    private final NotificadorDePublicaciones notificadorFalso = publicacion -> {
        notificadas.add(publicacion);
        return Mono.empty();
    };

    private final PublicarDesahogo publicarDesahogo = new PublicarDesahogo(repositorioFalso,
            aliasFijo, notificadorFalso);

    @Test
    void publicarYGuardarElDesahogo() {
        StepVerifier.create(publicarDesahogo.ejecutar("Dia largo", 15))
                .expectNextMatches(
                        p -> p.alias().equals("Alias de Prueba")
                                && p.mensaje().equals("Dia largo")
                )
                .verifyComplete();

        Assertions.assertEquals(1, guardadas.size());
    }

    @Test
    void notificaElDesahogoDespuesDeGuardarlo() {
        StepVerifier.create(
                        publicarDesahogo.ejecutar("Dia largo", 15)
                )
                .expectNextCount(1)
                .verifyComplete();

        Assertions.assertEquals(1, notificadas.size());
    }

    @Test
    void fallaSiElMensajeEstaVacio() {
        StepVerifier.create(
                        publicarDesahogo.ejecutar(" ", 15)
                )
                .expectError(PublicacionInvalidaException.class)
                .verify();
    }

    @Test
    void noNotificaSiElDesahogoEsInvalido() {
        StepVerifier.create(
                        publicarDesahogo.ejecutar(" ", 15)
                )
                .expectError(PublicacionInvalidaException.class)
                .verify();

        Assertions.assertTrue(notificadas.isEmpty());
        Assertions.assertTrue(guardadas.isEmpty());
    }


}
