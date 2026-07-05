package com.example.netbook.services;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.netbook.models.dto.AnotacionDTO;
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

    public ReporteService(ReporteRepository reporteRepository,
                        @Qualifier("estudiantesWebClient") WebClient webClientEstudiantes,
                        @Qualifier("academicoWebClient") WebClient webClientAcademico,
                        @Qualifier("hojaDeVidaWebClient") WebClient webClientHojaDeVida,
                        @Qualifier("anotacionesWebClient") WebClient webClientAnotaciones) {
        this.reporteRepository = reporteRepository;
        this.webClientEstudiantes = webClientEstudiantes;
        this.webClientAcademico = webClientAcademico;
        this.webClientHojaDeVida = webClientHojaDeVida;
        this.webClientAnotaciones = webClientAnotaciones;
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

    public List<EstudianteDTO> obtenerTodosLosEstudiantes() {
        try {
            return webClientEstudiantes.get()
                .uri("/estudiantes") // AJUSTAR cuando confirmes el path real
                .retrieve()
                .bodyToFlux(EstudianteDTO.class)
                .collectList()
                .block();
        } catch (Exception e) {
            log.error("Error al obtener estudiantes desde micro-estudiantes: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con Estudiantes");
        }
    }

    public EstudianteDTO obtenerEstudiantePorId(Integer idEstudiante) {
        try {
            return webClientEstudiantes.get()
                .uri("/estudiantes/" + idEstudiante) // AJUSTAR cuando confirmes el path real
                .retrieve()
                .bodyToMono(EstudianteDTO.class)
                .block();
        } catch (Exception e) {
            log.error("Error al obtener el estudiante {} : {}", idEstudiante, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado en el microservicio externo");
        }
    }

    public List<HojaDeVidaDTO> obtenerTodasLasHojasDeVida() {
        try {
            return webClientHojaDeVida.get()
                .uri("/hoja-de-vida") // AJUSTAR cuando confirmes el path real
                .retrieve()
                .bodyToFlux(HojaDeVidaDTO.class)
                .collectList()
                .block();
        } catch (Exception e) {
            log.error("Error al obtener hojas de vida desde micro-hoja-de-vida: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con Hoja de Vida");
        }
    }

    public HojaDeVidaDTO obtenerHojaDeVidaPorId(Integer idHojaDeVida) {
        try {
            return webClientHojaDeVida.get()
                .uri("/hoja-de-vida/" + idHojaDeVida) // AJUSTAR cuando confirmes el path real
                .retrieve()
                .bodyToMono(HojaDeVidaDTO.class)
                .block();
        } catch (Exception e) {
            log.error("Error al obtener la hoja de vida {} : {}", idHojaDeVida, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hoja de vida no encontrada en el microservicio externo");
        }
    }

    public List<AnotacionDTO> obtenerTodasLasAnotaciones() {
        try {
            return webClientAnotaciones.get()
                .uri("/anotaciones") // AJUSTAR cuando confirmes el path real
                .retrieve()
                .bodyToFlux(AnotacionDTO.class)
                .collectList()
                .block();
        } catch (Exception e) {
            log.error("Error al obtener anotaciones desde micro-anotaciones: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con Anotaciones");
        }
    }

    public AnotacionDTO obtenerAnotacionPorId(Integer idAnotacion) {
        try {
            return webClientAnotaciones.get()
                .uri("/anotaciones/" + idAnotacion) // AJUSTAR cuando confirmes el path real
                .retrieve()
                .bodyToMono(AnotacionDTO.class)
                .block();
        } catch (Exception e) {
            log.error("Error al obtener la anotación {} : {}", idAnotacion, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anotación no encontrada en el microservicio externo");
        }
    }

    // El bean "academicoWebClient" ya existe y queda inyectado (webClientAcademico) listo para usar
    // aquí mismo con el mismo patrón, apenas confirmes los endpoints reales de micro-academico
    // (notas, cursos, asignaturas, evaluaciones).

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