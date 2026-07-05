package com.example.netbook.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.request.AgregarEstudianteRequest;

@Service
public class EstudianteService {

    private final WebClient estudiantesWebClient;
    private final WebClient academicoWebClient;

    public EstudianteService(@Qualifier("estudiantesWebClient") WebClient estudiantesWebClient,
                             @Qualifier("academicoWebClient") WebClient academicoWebClient) {
        this.estudiantesWebClient = estudiantesWebClient;
        this.academicoWebClient = academicoWebClient;
    }

    public EstudianteDTO crearEstudiante(AgregarEstudianteRequest request) {
        if (request.getIdCurso() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El idCurso es obligatorio");
        }

        validarCursoExistente(request.getIdCurso());

        return estudiantesWebClient.post()
                .uri("/api/estudiantes")
                .bodyValue(Map.of(
                        "nombres", request.getNombres(),
                        "apellidoPaterno", request.getApellidoPaterno(),
                        "idCurso", request.getIdCurso()
                ))
                .retrieve()
                .bodyToMono(EstudianteDTO.class)
                .blockOptional()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo crear el estudiante"));
    }

    private void validarCursoExistente(Integer idCurso) {
        try {
            academicoWebClient.get()
                    .uri("/cursos/{idCurso}", idCurso)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un curso con idCurso=" + idCurso);
            }
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo validar el curso");
        }
    }
}
