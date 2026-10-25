package org.desempleadosanonimos.backend.infrastructure.web;

import org.desempleadosanonimos.backend.domain.model.TipoReaccion;

public record ReaccionarRequest(TipoReaccion tipoReaccion) {
}
