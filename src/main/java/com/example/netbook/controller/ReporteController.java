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
import org.springframework.web.bind.annotation.RestController;

import com.example.netbook.models.entities.Reporte;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.services.ReporteService;

@RequestMapping("reporte")
@RestController
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("")
    public List<Reporte> obtenerTodosLosReportes() {
        return reporteService.obtenerTodosLosReportes();
    }

    @GetMapping("/{idReporte}")
    public Reporte obtenerReportePorId(@PathVariable int idReporte) {
        return reporteService.obtenerReportePorId(idReporte);
    }

    @PostMapping("")
    public Reporte agregarReporte(@RequestBody AgregarReporte nueva) {
        return reporteService.agregarReporte(nueva);
    }
    @PutMapping("")
    public Reporte actualizarReporte(@RequestBody ActualizarReporte nueva) {
        return reporteService.actualizarReporte(nueva);
    }

    @DeleteMapping("/{idReporte}")
    public String eliminarReporte(@PathVariable int idReporte) {
        return reporteService.eliminarReporte(idReporte);
    }


}