package com.example.netbook.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.netbook.models.dto.CursoDTO;
import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.entities.FiltroReporte;
import com.example.netbook.models.request.ActualizarFiltroReporte;
import com.example.netbook.models.request.AgregarFiltroReporte;
import com.example.netbook.repositories.FiltroReporteRepository;

@Service
public class FiltroReporteService {

    private static final Logger log = LoggerFactory.getLogger(FiltroReporteService.class);

    private final FiltroReporteRepository filtroReporteRepository;
    private final WebClient webClientEstudiantes;

    public FiltroReporteService(FiltroReporteRepository filtroReporteRepository,
                                 @Qualifier("estudiantesWebClient") WebClient webClientEstudiantes) {
        this.filtroReporteRepository = filtroReporteRepository;
        this.webClientEstudiantes = webClientEstudiantes;
    }

    // =================================================================================
    // MÉTODOS ORIGINALES DE FILTRO REPORTE (CRUD local)
    // =================================================================================

    public List<FiltroReporte> obtenerTodoLoFiltroReporte() {
        return filtroReporteRepository.findAll();
    }

    public FiltroReporte obtenerFiltroReportePorId(int idFiltroReporte) {
        return filtroReporteRepository.findById(idFiltroReporte)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filtro no encontrado"));
    }

    public FiltroReporte agregarFiltroReporte(AgregarFiltroReporte nuevo) {
        FiltroReporte filtroReporteNuevo = new FiltroReporte();
        filtroReporteNuevo.setNombreAsignatura(nuevo.getNombreAsignatura());
        filtroReporteNuevo.setPeriodoInicio(nuevo.getPeriodoInicio());
        filtroReporteNuevo.setPeriodoFin(nuevo.getPeriodoFin());
        filtroReporteNuevo.setTipoReporte(nuevo.getTipoReporte());
        return filtroReporteRepository.save(filtroReporteNuevo);
    }

    public String eliminarFiltroReportePorId(int idFiltroReporte) {
        filtroReporteRepository.findById(idFiltroReporte)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filtro no encontrado"));

        filtroReporteRepository.deleteById(idFiltroReporte);
        return "Filtro eliminado correctamente";
    }

    public FiltroReporte actualizarFiltroReporte(ActualizarFiltroReporte nuevo) {
        FiltroReporte filtroReporte = filtroReporteRepository.findById(nuevo.getIdFiltroReporte())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filtro no encontrado."));

        filtroReporte.setNombreAsignatura(nuevo.getNombreAsignatura());
        filtroReporte.setPeriodoInicio(nuevo.getPeriodoInicio());
        filtroReporte.setPeriodoFin(nuevo.getPeriodoFin());
        filtroReporte.setTipoReporte(nuevo.getTipoReporte());

        return filtroReporteRepository.save(filtroReporte);
    }

    // =================================================================================
    // NUEVO: FILTRO DE ESTUDIANTES POR CURSO (llamada POST al microservicio Estudiantes)
    // =================================================================================

    // Helper con reintentos para POST que devuelven listas
    private <T, B> List<T> postListWithRetries(WebClient client, String uri, B body, Class<T> clazz, String serviceDesc) {
        int attempts = 3;
        for (int i = 1; i <= attempts; i++) {
            try {
                return client.post()
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToFlux(clazz)
                    .collectList()
                    .block();
            } catch (WebClientResponseException e) {
                log.warn("Intento {}/{} falló al hacer POST {} {}: {}", i, attempts, serviceDesc, uri, e.getStatusCode());
                if (i == attempts) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                }
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            } catch (Exception e) {
                log.warn("Error al hacer POST {} {}: {}, intento {}/{}", serviceDesc, uri, e.getMessage(), i, attempts);
                if (i == attempts) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
    }

    // AJUSTAR: "/api/estudiantes/curso" es el path SUPUESTO del microservicio Estudiantes.
    // Confirma el path real que expone ese controller para recibir idCurso por POST.
    public List<EstudianteDTO> obtenerEstudiantesPorCurso(CursoDTO curso) {
        return postListWithRetries(webClientEstudiantes, "/api/estudiantes/curso", curso, EstudianteDTO.class, "Estudiantes");
    }
}