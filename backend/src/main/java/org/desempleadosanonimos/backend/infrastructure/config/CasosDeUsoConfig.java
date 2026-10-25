package org.desempleadosanonimos.backend.infrastructure.config;

import org.desempleadosanonimos.backend.application.port.FuenteDePublicaciones;
import org.desempleadosanonimos.backend.application.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.application.port.NotificadorDePublicaciones;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.application.usecase.ListarDesahogos;
import org.desempleadosanonimos.backend.application.usecase.ObservarDesahogos;
import org.desempleadosanonimos.backend.application.usecase.PublicarDesahogo;
import org.desempleadosanonimos.backend.infrastructure.alias.GeneradorDeAliasAleatorio;
import org.desempleadosanonimos.backend.infrastructure.live.PublicacionesEnVivoConSink;
import org.desempleadosanonimos.backend.infrastructure.persistence.PublicacionRepositoryEnMemoria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CasosDeUsoConfig {

    @Bean
    public PublicacionRepository publicacionRepository() {
        return new PublicacionRepositoryEnMemoria();
    }

    @Bean
    public GeneradorDeAlias generadorDeAlias() {
        return new GeneradorDeAliasAleatorio();
    }

    @Bean
    public PublicacionesEnVivoConSink publicacionesEnVivoConSink() {
        return new PublicacionesEnVivoConSink();
    }

    @Bean
    public PublicarDesahogo publicarDesahogo(PublicacionRepository publicacionRepository,
                                             GeneradorDeAlias generadorDeAlias,
                                             NotificadorDePublicaciones notificadorDePublicaciones) {
        return new PublicarDesahogo(publicacionRepository, generadorDeAlias, notificadorDePublicaciones);
    }

    @Bean
    public ListarDesahogos listarDesahogos(PublicacionRepository publicacionRepository) {
        return new ListarDesahogos(publicacionRepository);
    }

    @Bean
    public ObservarDesahogos observarDesahogos(FuenteDePublicaciones fuenteDePublicaciones) {
        return new ObservarDesahogos(fuenteDePublicaciones);
    }
}
