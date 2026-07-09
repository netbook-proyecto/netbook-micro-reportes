package com.example.netbook.services;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.netbook.models.dto.AnotacionDTO;
import com.example.netbook.models.dto.AuthDTO;
import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.dto.HojaDeVidaDTO;
import com.example.netbook.models.dto.ReporteDTO;
import com.example.netbook.models.entities.Reporte;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.repositories.ReporteRepository;

@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;
    private final WebClient webClientEstudiantes;
    private final WebClient webClientAcademico;
    private final WebClient webClientHojaDeVida;
    private final WebClient webClientAnotaciones;
    private final WebClient webClientAuth;

    public ReporteService(ReporteRepository reporteRepository,
                        @Qualifier("estudiantesWebClient") WebClient webClientEstudiantes,
                        @Qualifier("academicoWebClient") WebClient webClientAcademico,
                        @Qualifier("hojaDeVidaWebClient") WebClient webClientHojaDeVida,
                        @Qualifier("anotacionesWebClient") WebClient webClientAnotaciones,
                        @Qualifier("authWebClient") WebClient webClientAuth) {
        this.reporteRepository = reporteRepository;
        this.webClientEstudiantes = webClientEstudiantes;
        this.webClientAcademico = webClientAcademico;
        this.webClientHojaDeVida = webClientHojaDeVida;
        this.webClientAnotaciones = webClientAnotaciones;
        this.webClientAuth = webClientAuth;
    }

    // --- Mapper: convierte la Entidad en DTO ---
    private ReporteDTO mapToDTO(Reporte reporte) {
        ReporteDTO dto = new ReporteDTO();
        dto.setIdReporte(reporte.getIdReporte());
        dto.setNombreReporte(reporte.getNombreReporte());
        dto.setTipoReporte(reporte.getTipoReporte());
        dto.setDescripcionReporte(reporte.getDescripcionReporte());
        dto.setEstadoReporte(reporte.getEstadoReporte());
        dto.setFechaReporte(reporte.getFechaReporte());
        return dto;
    }

    // =================================================================================
    // MÉTODOS PARA TRAER DATOS DE LOS MICROSERVICIOS QUE ALIMENTAN EL REPORTE
    // =================================================================================

    // Helper genérico con reintentos para GET simples
    private <T> T getWithRetries(WebClient client, String uri, Class<T> clazz, String serviceDesc) {
        int attempts = 3;
        for (int i = 1; i <= attempts; i++) {
            try {
                return client.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(clazz)
                    .block();
            } catch (WebClientResponseException e) {
                if (e.getStatusCode().is5xxServerError() || e.getStatusCode().value() == 404) {
                    log.warn("Intento {}/{} falló al solicitar {} {}: {}", i, attempts, serviceDesc, uri, e.getStatusCode());
                    if (i == attempts) {
                        if (e.getStatusCode().value() == 404) {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceDesc + " no encontrado en el microservicio externo");
                        }
                        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                    }
                    try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    continue;
                }
                log.error("Error al obtener {} {} : {}", serviceDesc, uri, e.getMessage());
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error en comunicación con " + serviceDesc);
            } catch (Exception e) {
                log.warn("Error al solicitar {} {}: {}, intento {}/{}", serviceDesc, uri, e.getMessage(), i, attempts);
                if (i == attempts) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
    }

    // Helper para llamadas que requieren Authorization header (ej. auth)
    private <T> T getWithRetriesAuth(WebClient client, String uri, String token, Class<T> clazz, String serviceDesc) {
        int attempts = 3;
        for (int i = 1; i <= attempts; i++) {
            try {
                return client.get()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(clazz)
                    .block();
            } catch (WebClientResponseException e) {
                log.warn("Intento {}/{} falló al solicitar {} {}: {}", i, attempts, serviceDesc, uri, e.getStatusCode());
                if (i == attempts) {
                    if (e.getStatusCode().value() == 404) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, serviceDesc + " no encontrado en el microservicio externo");
                    }
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                }
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            } catch (Exception e) {
                log.warn("Error al solicitar {} {}: {}, intento {}/{}", serviceDesc, uri, e.getMessage(), i, attempts);
                if (i == attempts) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
    }

    // Helper genérico con reintentos para GET que devuelven listas
    private <T> List<T> getListWithRetries(WebClient client, String uri, Class<T> clazz, String serviceDesc) {
        int attempts = 3;
        for (int i = 1; i <= attempts; i++) {
            try {
                return client.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToFlux(clazz)
                    .collectList()
                    .block();
            } catch (WebClientResponseException e) {
                log.warn("Intento {}/{} falló al solicitar lista {} {}: {}", i, attempts, serviceDesc, uri, e.getStatusCode());
                if (i == attempts) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                }
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            } catch (Exception e) {
                log.warn("Error al solicitar lista {} {}: {}, intento {}/{}", serviceDesc, uri, e.getMessage(), i, attempts);
                if (i == attempts) throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
                try { Thread.sleep(200L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con " + serviceDesc);
    }

    public List<EstudianteDTO> obtenerTodosLosEstudiantes() {
        return getListWithRetries(webClientEstudiantes, "/api/estudiantes", EstudianteDTO.class, "Estudiantes");
    }

    public EstudianteDTO obtenerEstudiantePorId(Integer idEstudiante) {
        return getWithRetries(webClientEstudiantes, "/api/estudiantes/" + idEstudiante, EstudianteDTO.class, "Estudiantes");
    }

    public List<HojaDeVidaDTO> obtenerTodasLasHojasDeVida() {
        return getListWithRetries(webClientHojaDeVida, "/hoja-de-vida", HojaDeVidaDTO.class, "HojaDeVida");
    }

    public HojaDeVidaDTO obtenerHojaDeVidaPorId(Integer idHojaDeVida) {
        return getWithRetries(webClientHojaDeVida, "/hoja-de-vida/" + idHojaDeVida, HojaDeVidaDTO.class, "HojaDeVida");
    }

    public List<AnotacionDTO> obtenerTodasLasAnotaciones() {
        // AJUSTAR: path puesto como referencia. Confirma el path real del controller de micro-anotaciones.
        return getListWithRetries(webClientAnotaciones, "/anotaciones", AnotacionDTO.class, "Anotaciones");
    }

    public AnotacionDTO obtenerAnotacionPorId(Integer idAnotacion) {
        // AJUSTAR: path puesto como referencia. Confirma el path real del controller de micro-anotaciones.
        return getWithRetries(webClientAnotaciones, "/anotaciones/" + idAnotacion, AnotacionDTO.class, "Anotaciones");
    }

    // AJUSTAR: path puesto como referencia ("/auth/validate"). Cambialo por el endpoint
    // real que exponga tu microservicio de auth para validar token / usuario.
    public AuthDTO validarConexionAuth(String token) {
        return getWithRetriesAuth(webClientAuth, "/auth/validate", token, AuthDTO.class, "Auth");
    }

    // =================================================================================
    // MÉTODOS ORIGINALES DE REPORTE
    // =================================================================================

    public List<ReporteDTO> obtenerTodosLosReportes() {
        return reporteRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .toList();
    }

    public ReporteDTO obtenerReportePorId(int idReporte) {
        Reporte reporte = reporteRepository.findById(idReporte)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
        return mapToDTO(reporte);
    }

    public ReporteDTO agregarReporte(AgregarReporte nueva) {
        Reporte reporte = new Reporte();
        reporte.setNombreReporte(nueva.getNombreReporte());
        reporte.setTipoReporte(nueva.getTipoReporte());
        reporte.setDescripcionReporte(nueva.getDescripcionReporte());
        reporte.setEstadoReporte(nueva.getEstadoReporte());
        reporte.setFechaReporte(LocalDate.now());

        Reporte reporteGuardado = reporteRepository.save(reporte);
        return mapToDTO(reporteGuardado);
    }

    public ReporteDTO actualizarReporte(ActualizarReporte nueva) {
        Reporte reporte = reporteRepository.findById(nueva.getIdReporte())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
        reporte.setNombreReporte(nueva.getNombreReporte());
        reporte.setTipoReporte(nueva.getTipoReporte());
        reporte.setDescripcionReporte(nueva.getDescripcionReporte());
        reporte.setEstadoReporte(nueva.getEstadoReporte());

        Reporte reporteActualizado = reporteRepository.save(reporte);
        return mapToDTO(reporteActualizado);
    }

    public String eliminarReporte(int idReporte) {
        reporteRepository.findById(idReporte)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
        reporteRepository.deleteById(idReporte);
        return "Reporte eliminado correctamente";
    }
}