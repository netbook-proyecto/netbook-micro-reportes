package com.example.netbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "reporteWebClient")
    public WebClient reporteWebClient() {
        // Corregido: Quitamos "/reporte.html" para que sea la URL base del microservicio
        return WebClient.builder()
                .baseUrl("http://localhost:5007")
                .build();
    }

    @Bean(name = "mensajeriaWebClient")
    public WebClient mensajeriaWebClient() {
        // Corregido: Apuntamos a la raíz del puerto 5008 de mensajería
        return WebClient.builder()
                .baseUrl("http://localhost:5008")
                .build();
    }

    @Bean(name = "academicoWebClient")
    public WebClient academicoWebClient() {
        // Corregido: Quitamos "/academico.html"
        return WebClient.builder()
                .baseUrl("http://localhost:5004")
                .build();
    }
}