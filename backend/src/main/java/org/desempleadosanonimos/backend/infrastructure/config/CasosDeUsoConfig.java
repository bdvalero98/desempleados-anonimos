package org.desempleadosanonimos.backend.infrastructure.config;

import org.desempleadosanonimos.backend.application.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.application.port.PublicacionRepository;
import org.desempleadosanonimos.backend.application.usecase.ListarDesahogos;
import org.desempleadosanonimos.backend.application.usecase.PublicarDesahogo;
import org.desempleadosanonimos.backend.infrastructure.alias.GeneradorDeAliasAleatorio;
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
    public PublicarDesahogo publicarDeshago(PublicacionRepository publicacionRepository,
                                            GeneradorDeAlias generadorDeAlias) {
        return new PublicarDesahogo(publicacionRepository, generadorDeAlias);
    }

    @Bean
    public ListarDesahogos listarDeshagos(PublicacionRepository publicacionRepository) {
        return new ListarDesahogos(publicacionRepository);
    }
}
