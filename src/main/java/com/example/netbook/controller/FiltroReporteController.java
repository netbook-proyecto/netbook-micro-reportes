package com.example.netbook.controller;


import java.util.List;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.netbook.models.dto.CursoDTO;
import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.entities.FiltroReporte;
import com.example.netbook.models.request.ActualizarFiltroReporte;
import com.example.netbook.models.request.AgregarFiltroReporte;
import com.example.netbook.services.FiltroReporteService;

@RequestMapping("filtroReporte")
@RestController
public class FiltroReporteController {

    private final FiltroReporteService filtroreporteService;

    public FiltroReporteController(FiltroReporteService filtroreporteService) {
        this.filtroreporteService = filtroreporteService;
    }

    @GetMapping("")
    public List<FiltroReporte> obtenerTodosLosFiltroReporte() {
        return filtroreporteService.obtenerTodoLoFiltroReporte();
    }

    @GetMapping("/{idFiltroReporte}")
    public FiltroReporte obtenerFiltroReportePorId(@PathVariable int idFiltroReporte) {
        return filtroreporteService.obtenerFiltroReportePorId(idFiltroReporte);
    }

    @PostMapping("")
    public FiltroReporte agregarFiltroReporte(@Valid @RequestBody AgregarFiltroReporte nuevo) {
        return filtroreporteService.agregarFiltroReporte(nuevo);
    }
    
    @PutMapping("")
    public FiltroReporte actualizarFiltroReporte(@Valid @RequestBody ActualizarFiltroReporte nuevo) {
        return filtroreporteService.actualizarFiltroReporte(nuevo);
    }

    @DeleteMapping("/{idFiltroReporte}")
    public String eliminarFiltroReportePorId(@PathVariable int idFiltroReporte) {
        return filtroreporteService.eliminarFiltroReportePorId(idFiltroReporte);
    }

    // =================================================================================
    // NUEVO: filtro de estudiantes por curso (POST hacia microservicio Estudiantes)
    // =================================================================================

    @PostMapping("/estudiantes/curso")
    public List<EstudianteDTO> obtenerEstudiantesPorCurso(@Valid @RequestBody CursoDTO curso) {
        return filtroreporteService.obtenerEstudiantesPorCurso(curso);
    }

}