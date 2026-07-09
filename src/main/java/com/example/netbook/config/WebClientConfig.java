package com.example.netbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "reporteWebClient")
    public WebClient reporteWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:5007")
                .build();
    }

    @Bean(name = "estudiantesWebClient")
    public WebClient estudiantesWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:5002")
                .build();
    }

    @Bean(name = "academicoWebClient")
    public WebClient academicoWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:5004")
                .build();
    }

    @Bean(name = "hojaDeVidaWebClient")
    public WebClient hojaDeVidaWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:5009")
                .build();
    }

    @Bean(name = "anotacionesWebClient")
    public WebClient anotacionesWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:5005")
                .build();
    }

    // en el application.properties del microservicio de auth.
    @Bean(name = "authWebClient")
    public WebClient authWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:5001")
                .build();
    }
}