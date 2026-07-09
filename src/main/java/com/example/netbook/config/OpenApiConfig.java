package com.example.netbook.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Value("${app.name:Gestión Reporte}")
        private String appName;

        @Value("${app.version:1.0.1}")
        private String appVersion;

        @Bean
        public OpenAPI netbookOpenAPI() {
                return new OpenAPI()
                        .info(new Info()
                                .title("API de Gestión de Reportes - netBOOK")
                                .description("Microservicio central para la administración del directorio demográfico de la plataforma escolar. Construido bajo arquitectura de capas con validaciones DTO y persistencia en MySQL.")
                                .version("1.0.0")
        );
        }
}