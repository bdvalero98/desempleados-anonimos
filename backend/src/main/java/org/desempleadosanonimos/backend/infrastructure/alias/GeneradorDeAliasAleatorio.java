package org.desempleadosanonimos.backend.infrastructure.alias;

import org.desempleadosanonimos.backend.aplication.port.GeneradorDeAlias;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneradorDeAliasAleatorio implements GeneradorDeAlias {

    private static final List<String> ANIMALES = List.of(
            "Gato",
            "Panda",
            "Buho",
            "Zorro",
            "Pingüino",
            "Capibara",
            "Nutria",
            "Lobo",
            "Tortuga"
    );

    private static final List<String> ADJETIVOS = List.of(
            "Feliz",
            "Triste",
            "Enojado",
            "Dormilón",
            "Perezoso",
            "Travieso",
            "Valiente",
            "Curioso",
            "Simpático"
    );

    @Override
    public String generar() {
        ThreadLocalRandom azar = ThreadLocalRandom.current();

        String animal = ANIMALES.get(azar.nextInt(ANIMALES.size()));
        String adjetivo = ADJETIVOS.get(azar.nextInt(ADJETIVOS.size()));

        int numero = azar.nextInt(1, 100);

        return animal + " " + adjetivo + " #" + numero;
    }
}
