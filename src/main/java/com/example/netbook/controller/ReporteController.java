package com.example.netbook.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.dto.ReporteDTO;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.services.ReporteService;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public List<ReporteDTO> obtenerTodosLosReportes() {
        return reporteService.obtenerTodosLosReportes();
    }

    @GetMapping("/{idReporte}")
    public ReporteDTO obtenerReportePorId(@PathVariable("idReporte") int idReporte) {
        return reporteService.obtenerReportePorId(idReporte);
    }

    @PostMapping
    public ReporteDTO agregarReporte(@Valid @RequestBody AgregarReporte nueva) {
        return reporteService.agregarReporte(nueva);
    }

    @PutMapping("/{idReporte}")
    public ReporteDTO actualizarReporte(
        @PathVariable("idReporte") int idReporte,
        @Valid @RequestBody ActualizarReporte nueva) {
        nueva.setIdReporte(idReporte);
        return reporteService.actualizarReporte(nueva);
}
    
    @DeleteMapping("/{idReporte}")
    public String eliminarReporte(@PathVariable("idReporte") int idReporte) {
        return reporteService.eliminarReporte(idReporte);
    }

    // =================================================================================
    // ENDPOINTS DE SOLO LECTURA hacia el microservicio Estudiantes.
    // Reporte NO crea estudiantes (eso vive en el microservicio Estudiantes);
    // aquí solo se consultan datos para alimentar los reportes.
    // Llaman a los métodos de ReporteService que ya hacían la llamada WebClient
    // pero que hasta ahora no estaban conectados a ningún endpoint.
    // =================================================================================

    @GetMapping("/estudiantes")
    public List<EstudianteDTO> obtenerTodosLosEstudiantes() {
        return reporteService.obtenerTodosLosEstudiantes();
    }

    @GetMapping("/estudiantes/{idEstudiante}")
    public EstudianteDTO obtenerEstudiantePorId(@PathVariable("idEstudiante") Integer idEstudiante) {
        return reporteService.obtenerEstudiantePorId(idEstudiante);
    }
}