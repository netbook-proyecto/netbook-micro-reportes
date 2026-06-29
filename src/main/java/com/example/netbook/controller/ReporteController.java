package com.example.netbook.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.netbook.models.dto.ReporteDTO;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.services.ReporteService;

@RestController
@RequestMapping("/reportes") // Asegúrate de que esta ruta sea la correspondiente en tu controlador
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

    // CORRECCIÓN AQUÍ: Cambiamos el tipo de retorno de ReporteDTO a Map<String, Object>
    @PostMapping
    public Map<String, Object> agregarReporte(@RequestBody AgregarReporte nueva) {
        return reporteService.agregarReporte(nueva);
    }

    @PutMapping("/{idReporte}")
    public ReporteDTO actualizarReporte(
        @PathVariable("idReporte") int idReporte,
        @RequestBody ActualizarReporte nueva) {
        nueva.setIdReporte(idReporte); // El ID viene de la URL, no del body
        return reporteService.actualizarReporte(nueva);
}
    
    @DeleteMapping("/{idReporte}")
    public String eliminarReporte(@PathVariable("idReporte") int idReporte) {
        return reporteService.eliminarReporte(idReporte);
    }
}