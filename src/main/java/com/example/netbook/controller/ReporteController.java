package com.example.netbook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.netbook.models.dto.AnotacionDTO;
import com.example.netbook.models.dto.AuthDTO;
import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.dto.HojaDeVidaDTO;
import com.example.netbook.models.dto.ReporteDTO;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.services.ReporteService;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // =================================================================================
    // ENDPOINTS DE PRUEBA DE CONECTIVIDAD ENTRE MICROSERVICIOS
    // =================================================================================

    @GetMapping("/test/estudiantes")
    public List<EstudianteDTO> testConexionEstudiantes() {
        return reporteService.obtenerTodosLosEstudiantes();
    }

    @GetMapping("/test/estudiantes/{idEstudiante}")
    public EstudianteDTO testConexionEstudiantePorId(@PathVariable("idEstudiante") Integer idEstudiante) {
        return reporteService.obtenerEstudiantePorId(idEstudiante);
    }

    @GetMapping("/test/hoja-de-vida")
    public List<HojaDeVidaDTO> testConexionHojaDeVida() {
        return reporteService.obtenerTodasLasHojasDeVida();
    }

    @GetMapping("/test/hoja-de-vida/{idHojaDeVida}")
    public HojaDeVidaDTO testConexionHojaDeVidaPorId(@PathVariable("idHojaDeVida") Integer idHojaDeVida) {
        return reporteService.obtenerHojaDeVidaPorId(idHojaDeVida);
    }

    @GetMapping("/test/anotaciones")
    public List<AnotacionDTO> testConexionAnotaciones() {
        return reporteService.obtenerTodasLasAnotaciones();
    }

    @GetMapping("/test/anotaciones/{idAnotacion}")
    public AnotacionDTO testConexionAnotacionPorId(@PathVariable("idAnotacion") Integer idAnotacion) {
        return reporteService.obtenerAnotacionPorId(idAnotacion);
    }

    // AJUSTAR: recibe el token que te devuelva el login de tu micro-auth
    @GetMapping("/test/auth")
    public AuthDTO testConexionAuth(@RequestParam("token") String token) {
        return reporteService.validarConexionAuth(token);
    }

    @GetMapping
    public List<ReporteDTO> obtenerTodosLosReportes() {
        return reporteService.obtenerTodosLosReportes();
    }

    @GetMapping("/{idReporte}")
    public ReporteDTO obtenerReportePorId(@PathVariable("idReporte") int idReporte) {
        return reporteService.obtenerReportePorId(idReporte);
    }

    @PostMapping
    public ReporteDTO agregarReporte(@RequestBody AgregarReporte nueva) {
        return reporteService.agregarReporte(nueva);
    }

    @PutMapping("/{idReporte}")
    public ReporteDTO actualizarReporte(
        @PathVariable("idReporte") int idReporte,
        @RequestBody ActualizarReporte nueva) {
        nueva.setIdReporte(idReporte);
        return reporteService.actualizarReporte(nueva);
    }

    @DeleteMapping("/{idReporte}")
    public String eliminarReporte(@PathVariable("idReporte") int idReporte) {
        return reporteService.eliminarReporte(idReporte);
    }
}