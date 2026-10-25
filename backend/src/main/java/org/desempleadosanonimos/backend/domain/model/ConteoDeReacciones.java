package org.desempleadosanonimos.backend.domain.model;

public record ConteoDeReacciones(int yoTambien, int animo) {

    public static ConteoDeReacciones vacio() {
        return new ConteoDeReacciones(0, 0);
    }

    public ConteoDeReacciones incrementar(TipoReaccion tipoReaccion) {
        return switch (tipoReaccion) {
            case YO_TAMBIEN -> new ConteoDeReacciones(yoTambien + 1, animo);
            case ANIMO -> new ConteoDeReacciones(yoTambien, animo + 1);
        };
    }
}
