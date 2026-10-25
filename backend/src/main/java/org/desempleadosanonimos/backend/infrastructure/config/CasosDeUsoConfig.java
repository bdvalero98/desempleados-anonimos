package org.desempleadosanonimos.backend.infrastructure.config;

import org.desempleadosanonimos.backend.aplication.port.GeneradorDeAlias;
import org.desempleadosanonimos.backend.aplication.port.PublicacionRepository;
import org.desempleadosanonimos.backend.aplication.usecase.ListarDeshagos;
import org.desempleadosanonimos.backend.aplication.usecase.PublicarDeshago;
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
    public PublicarDeshago publicarDeshago(PublicacionRepository publicacionRepository,
                                           GeneradorDeAlias generadorDeAlias) {
        return new PublicarDeshago(publicacionRepository, generadorDeAlias);
    }

    @Bean
    public ListarDeshagos listarDeshagos(PublicacionRepository publicacionRepository) {
        return new ListarDeshagos(publicacionRepository);
    }
}
