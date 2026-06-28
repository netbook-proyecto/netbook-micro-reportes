package com.example.netbook.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.netbook.models.dto.MensajeriaDTO; // Importante importar el DTO
import com.example.netbook.models.dto.ReporteDTO;
import com.example.netbook.models.entities.Reporte;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.repositories.ReporteRepository;

@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;
    private final WebClient webClientMensajeria;

    public ReporteService(ReporteRepository reporteRepository,
                        @Qualifier("mensajeriaWebClient") WebClient webClientMensajeria) {
        this.reporteRepository = reporteRepository;
        this.webClientMensajeria = webClientMensajeria;
    }

    // --- Mapper: convierte la Entidad en DTO ---
    private ReporteDTO mapToDTO(Reporte reporte) {
        return new ReporteDTO(
            reporte.getIdReporte(),
            reporte.getNombreReporte(),
            reporte.getTipoReporte(),
            reporte.getDescripcionReporte(),
            reporte.getEstadoReporte(),
            reporte.getFechaReporte()
        );
    }

    // =================================================================================
    // MÉTODOS PARA LLAMAR AL MICROSERVICIO DE MENSAJERÍA Y TRAER MensajeriaDTO
    // =================================================================================

    /**
     * Obtiene todos los mensajes llamando al microservicio de mensajería.
     */
    public List<MensajeriaDTO> obtenerTodosLosMensajes() {
        try {
            return webClientMensajeria.get()
                .uri("/mensajerias") // Corregido: antes apuntaba a /mensajes
                .retrieve()
                .bodyToFlux(MensajeriaDTO.class)
                .collectList()
                .block();
        } catch (Exception e) {
            log.error("Error al obtener mensajes desde el microservicio de mensajería: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo contactar con Mensajería");
        }
    }

    /**
     * Obtiene un mensaje en específico por su ID desde el microservicio de mensajería.
     */
    public MensajeriaDTO obtenerMensajePorId(Integer idMensaje) {
        try {
            return webClientMensajeria.get()
                .uri("/mensajerias/" + idMensaje) // Corregido: antes apuntaba a /mensajes/id
                .retrieve()
                .bodyToMono(MensajeriaDTO.class)
                .block(); 
        } catch (Exception e) {
            log.error("Error al obtener el mensaje {} : {}", idMensaje, e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensaje no encontrado en el microservicio externo");
        }
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

    // Se cambia el retorno a Map<String, Object> para poder devolver el ReporteDTO y el ID del mensaje
    public Map<String, Object> agregarReporte(AgregarReporte nueva) {
        Reporte reporte = new Reporte();
        reporte.setNombreReporte(nueva.getNombreReporte());
        reporte.setTipoReporte(nueva.getTipoReporte());
        reporte.setDescripcionReporte(nueva.getDescripcionReporte());
        reporte.setEstadoReporte(nueva.getEstadoReporte());
        reporte.setFechaReporte(LocalDate.now());

        Reporte reporteGuardado = reporteRepository.save(reporte);

        Map<String, Object> notificacion = new HashMap<>();
        notificacion.put("asunto", "Nuevo Reporte: " + reporteGuardado.getNombreReporte());
        notificacion.put("cuerpoMensaje",
            "Se generó el reporte de tipo '" + reporteGuardado.getTipoReporte() +
            "' con estado: " + reporteGuardado.getEstadoReporte() +
            ". Descripción: " + reporteGuardado.getDescripcionReporte()
        );
        notificacion.put("estadoLectura", "NO_LEIDO");

        Integer idMensajeGenerado = null;

        try {
            // Se cambia bodyToMono a MensajeriaDTO.class para capturar la respuesta
            MensajeriaDTO respuestaMensajeria = webClientMensajeria.post()
                .uri("/mensajerias") // Corregido: antes apuntaba a /notificaciones (Daba error 404)
                .bodyValue(notificacion)
                .retrieve()
                .bodyToMono(MensajeriaDTO.class)
                .block();

            if (respuestaMensajeria != null) {
                // Obtenemos el ID generado desde el record MensajeriaDTO
                idMensajeGenerado = respuestaMensajeria.idMensaje();
            }
        } catch (Exception e) {
            log.warn("Advertencia: no se pudo notificar a Mensajería: {}", e.getMessage());
        }

        // Preparamos la respuesta que incluye el reporte y la confirmación de la mensajería
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("reporte", mapToDTO(reporteGuardado));
        resultado.put("idMensajeCreado", idMensajeGenerado); // Será null si hubo un error en la conexión

        return resultado;
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