package org.desempleadosanonimos.backend.application.usecase;

import org.desempleadosanonimos.backend.application.port.NotificadorDePublicaciones;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.domain.exception.PublicacionNoEncontradaException;
import org.desempleadosanonimos.backend.domain.model.Publicacion;
import org.desempleadosanonimos.backend.domain.model.TipoReaccion;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReaccionarADesahogoTest {

    private final Map<UUID, Publicacion> almacen = new HashMap<>();
    private final List<Publicacion> notificadas = new ArrayList<>();

    private final PublicacionRepository repositorioFalso = new PublicacionRepository() {
        @Override
        public Mono<Publicacion> guardar(Publicacion publicacion) {
            almacen.put(publicacion.id(), publicacion);
            return Mono.just(publicacion);
        }

        @Override
        public Flux<Publicacion> listarRecientes() {
            return Flux.fromIterable(almacen.values());
        }

        @Override
        public Mono<Publicacion> modificar(UUID id, UnaryOperator<Publicacion> cambio) {
            return Mono.fromSupplier(
                    () -> almacen.computeIfPresent(id, (clave, actual) -> cambio.apply(actual))
            );
        }
    };

    private final NotificadorDePublicaciones notificadorFalso = publicacion -> {
        notificadas.add(publicacion);
        return Mono.empty();
    };

    private final ReaccionarADesahogo reaccionar = new ReaccionarADesahogo(repositorioFalso, notificadorFalso);

    private Publicacion existente() {
        Publicacion publicacion = Publicacion.crear("Gato Terco #7", "Seguimos", null);
        almacen.put(publicacion.id(), publicacion);
        return publicacion;
    }

    @Test
    void sumaLaReaccionAlContadorCorrecto() {
        Publicacion publicacion = existente();

        StepVerifier.create(
                        reaccionar.ejecutar(publicacion.id(), TipoReaccion.ANIMO)
                )
                .expectNextMatches(
                        p -> p.conteoDeReacciones().animo() == 1
                                && p.conteoDeReacciones().yoTambien() == 0
                )
                .verifyComplete();
    }

    @Test
    void acumularVariasReacciones() {
        Publicacion publicacion = existente();

        reaccionar.ejecutar(publicacion.id(), TipoReaccion.YO_TAMBIEN).block();
        reaccionar.ejecutar(publicacion.id(), TipoReaccion.YO_TAMBIEN).block();

        assertEquals(2, almacen.get(publicacion.id()).conteoDeReacciones().yoTambien());
    }

    @Test
    void notificaLaPublicacionActualizada() {
        Publicacion publicacion = existente();

        StepVerifier.create(
                        reaccionar.ejecutar(publicacion.id(), TipoReaccion.ANIMO)
                )
                .expectNextCount(1)
                .verifyComplete();

        assertEquals(1, notificadas.size());
        assertEquals(1, notificadas.getFirst().conteoDeReacciones().animo());
    }

    @Test
    void fallaSiLaPublicacionNoExiste() {
        StepVerifier.create(
                        reaccionar.ejecutar(UUID.randomUUID(), TipoReaccion.ANIMO)
                )
                .expectError(PublicacionNoEncontradaException.class)
                .verify();

        assertTrue(notificadas.isEmpty());
    }
}