package org.desempleadosanonimos.backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

    private final String[] originesPermitidos;

    public CorsConfig(@Value("${app.cors.origenes}") String[] originesPermitidos) {
        this.originesPermitidos = originesPermitidos;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(originesPermitidos)
                .allowedMethods("GET", "POST")
                .allowedHeaders("*");
    }
}
