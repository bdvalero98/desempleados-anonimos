package org.desempleadosanonimos.backend.infrastructure.web;

import org.desempleadosanonimos.backend.domain.model.Publicacion;

import java.time.Instant;
import java.util.UUID;

public record PublicacionResponse(UUID id, String alias, String mensaje,
                                  Integer diasDesempleado, Instant creadaEn,
                                  int yoTambien, int animo) {

    public static PublicacionResponse desde(Publicacion publicacion) {
        return new PublicacionResponse(
                publicacion.id(),
                publicacion.alias(),
                publicacion.mensaje(),
                publicacion.diasDesempleado(),
                publicacion.creadaEn(),
                publicacion.conteoDeReacciones().yoTambien(),
                publicacion.conteoDeReacciones().animo()
        );
    }
}
