package com.example.netbook;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.request.AgregarEstudianteRequest;
import com.example.netbook.services.EstudianteService;
import com.sun.net.httpserver.HttpServer;

class EstudianteServiceTest {

    private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void deberiaCrearEstudianteCuandoElCursoExiste() throws Exception {
        AtomicInteger cursoRequests = new AtomicInteger();
        AtomicInteger estudianteRequests = new AtomicInteger();

        server.createContext("/cursos/99", exchange -> {
            cursoRequests.incrementAndGet();
            byte[] response = "{\"idCurso\":99,\"nombreCurso\":\"Matemáticas\"}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        });

        server.createContext("/api/estudiantes", exchange -> {
            estudianteRequests.incrementAndGet();
            byte[] response = "{\"idEstudiante\":7,\"nombres\":\"Ana\",\"apellidoPaterno\":\"Pérez\"}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        });

        server.start();

        String baseUrl = "http://localhost:" + server.getAddress().getPort();
        WebClient estudiantesWebClient = WebClient.builder().baseUrl(baseUrl).build();
        WebClient academicoWebClient = WebClient.builder().baseUrl(baseUrl).build();
        EstudianteService service = new EstudianteService(estudiantesWebClient, academicoWebClient);

        AgregarEstudianteRequest request = new AgregarEstudianteRequest();
        request.setNombres("Ana");
        request.setApellidoPaterno("Pérez");
        request.setIdCurso(99);

        EstudianteDTO estudiante = service.crearEstudiante(request);

        assertThat(estudiante.getIdEstudiante()).isEqualTo(7);
        assertThat(estudiante.getNombres()).isEqualTo("Ana");
        assertThat(cursoRequests.get()).isEqualTo(1);
        assertThat(estudianteRequests.get()).isEqualTo(1);
    }
}
