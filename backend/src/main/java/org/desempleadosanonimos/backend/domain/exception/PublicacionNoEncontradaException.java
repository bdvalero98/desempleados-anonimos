package org.desempleadosanonimos.backend.domain.exception;

import java.util.UUID;

public class PublicacionNoEncontradaException extends RuntimeException {

    public PublicacionNoEncontradaException(UUID id) {
        super("No existe la publicación " + id);
    }
}
