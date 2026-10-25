package org.desempleadosanonimos.backend.domain.model;

import org.desempleadosanonimos.backend.domain.exception.PublicacionInvalidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicacionTest {

    @Test
    void creaUnaPublicacionValida() {
        Publicacion publicacion = Publicacion
                .crear("Gato Pensativo", "la prostitucion no suena tan mal", 60);

        assertNotNull(publicacion.id());
        assertEquals("la prostitucion no suena tan mal", publicacion.mensaje());
        assertEquals(Integer.valueOf(60), publicacion.diasDesempleado());
    }

    @Test
    void rechazaUnMensajeVacio() {
        assertThrows(PublicacionInvalidaException.class,
                () -> Publicacion.crear("Gato Pensativo", "", 60));
    }

    @Test
    void RechazaUnMensajeDemasiadoLargo() {
        String mensajeLargo = "a".repeat(501);

        assertThrows(PublicacionInvalidaException.class,
                () -> Publicacion.crear("Gato Pensativo #42", mensajeLargo, 10));
    }

    @Test
    void rechazaDiasNegativos() {

        assertThrows(PublicacionInvalidaException.class,
                () -> Publicacion.crear("Gato Pensativo", "la prostitucion no suena tan mal", -1));
    }

    @Test
    void reaccionarDevuelveUnaCopiaSinModificarLaOriginal() {
        Publicacion original = Publicacion.crear("Gato Terco #7", "Seguimos", null);

        Publicacion conReaccion = original.reaccionar(TipoReaccion.YO_TAMBIEN);

        assertEquals(0, original.conteoDeReacciones().yoTambien());
        assertEquals(1, conReaccion.conteoDeReacciones().yoTambien());
        assertEquals(original.id(), conReaccion.id());
    }
}