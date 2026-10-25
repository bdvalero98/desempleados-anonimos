package org.desempleadosanonimos.backend.infrastructure.web;

import org.desempleadosanonimos.backend.domain.exception.PublicacionInvalidaException;
import org.desempleadosanonimos.backend.domain.exception.PublicacionNoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorDeErrores {

    public record ErrorResponse(String mensaje) {
    }

    @ExceptionHandler(PublicacionInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse publicacionInvalida(PublicacionInvalidaException error) {
        return new ErrorResponse(error.getMessage());
    }

    @ExceptionHandler(PublicacionNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse publicacionNoEncontrada(PublicacionNoEncontradaException error) {
        return new ErrorResponse(error.getMessage());
    }
}
