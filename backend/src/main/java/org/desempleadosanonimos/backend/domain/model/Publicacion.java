package org.desempleadosanonimos.backend.domain.model;

import org.desempleadosanonimos.backend.domain.exception.PublicacionInvalidaException;

import java.time.Instant;
import java.util.UUID;

public class Publicacion {

    private static final int LARGO_MAXIMO_MENSAJE = 500;

    private final UUID id;
    private final String alias;
    private final String mensaje;
    private final Integer diasDesempleado;
    private final Instant creadaEn;
    private final ConteoDeReacciones conteoDeReacciones;

    private Publicacion(UUID id, String alias, String mensaje, Integer diasDesempleado,
                        Instant creadaEn, ConteoDeReacciones conteoDeReacciones) {
        this.id = id;
        this.alias = alias;
        this.mensaje = mensaje;
        this.diasDesempleado = diasDesempleado;
        this.creadaEn = creadaEn;
        this.conteoDeReacciones = conteoDeReacciones;
    }

    public static Publicacion crear(String alias, String mensaje, Integer diasDesempleado) {
        validarAlias(alias);
        validarMensaje(mensaje);
        validarDiasDesempleado(diasDesempleado);

        return new Publicacion(UUID.randomUUID(), alias, mensaje.strip(),
                diasDesempleado, Instant.now(), ConteoDeReacciones.vacio());
    }

    public static Publicacion reconstruir(UUID id, String alias, String mensaje, Integer diasDesempleado,
                                          Instant creadaEn, ConteoDeReacciones conteoDeReacciones) {

        return new Publicacion(id, alias, mensaje, diasDesempleado, creadaEn, conteoDeReacciones);
    }

    public Publicacion reaccionar(TipoReaccion tipoReaccion) {

        if (tipoReaccion == null) {
            throw new PublicacionInvalidaException("Debe indicar el tipo de reacción");
        }

        return new Publicacion(id, alias, mensaje,
                diasDesempleado, creadaEn, conteoDeReacciones.incrementar(tipoReaccion));
    }

    private static void validarAlias(String alias) {

        if (alias == null || alias.isBlank()) {
            throw new PublicacionInvalidaException("El alias no puede ser nulo o vacío");
        }
    }

    private static void validarMensaje(String mensaje) {

        if (mensaje == null || mensaje.isBlank()) {
            throw new PublicacionInvalidaException("El mensaje no puede ser nulo o vacío");
        }

        if (mensaje.strip().length() > LARGO_MAXIMO_MENSAJE) {
            throw new PublicacionInvalidaException("El mensaje no puede superar los " + LARGO_MAXIMO_MENSAJE + " caracteres");
        }
    }

    private static void validarDiasDesempleado(Integer diasDesempleado) {

        if (diasDesempleado != null && diasDesempleado < 0) {
            throw new PublicacionInvalidaException("Los días desempleado no pueden ser negativos");
        }
    }

    public UUID id() {
        return id;
    }

    public String alias() {
        return alias;
    }

    public String mensaje() {
        return mensaje;
    }

    public Integer diasDesempleado() {
        return diasDesempleado;
    }

    public Instant creadaEn() {
        return creadaEn;
    }

    public ConteoDeReacciones conteoDeReacciones() {
        return conteoDeReacciones;
    }
}
