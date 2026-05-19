package com.example.netbook.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.netbook.models.entities.Reporte;
import com.example.netbook.models.request.ActualizarReporte;
import com.example.netbook.models.request.AgregarReporte;
import com.example.netbook.repositories.ReporteRepository;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;


    public List<Reporte> obtenerTodosLosReportes() {
        return reporteRepository.findAll();
    }

    public Reporte obtenerReportePorId(int idReporte) {
        Reporte reporte =  reporteRepository.findById(idReporte).orElse(null);
        if (reporte == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado");
        }
        return reporte;
    }

    public Reporte agregarReporte(AgregarReporte nueva){
        Reporte reporteNueva = new Reporte();
        reporteNueva.setNombreReporte(nueva.getNombreReporte());
        reporteNueva.setTipoReporte(nueva.getTipoReporte());
        reporteNueva.setDescripcionReporte(nueva.getDescripcionReporte());
        reporteNueva.setEstadoReporte(nueva.getEstadoReporte());
        return reporteRepository.save(reporteNueva);
        
    }

    public String eliminarReporte(int idReporte) {
        if(reporteRepository.existsById(idReporte)) {
            reporteRepository.deleteById(idReporte);
            return "Reporte eliminado correctamente.";
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado");
        }
    }


    public Reporte actualizarReporte(ActualizarReporte nuevaReporte){
        Reporte reporte = reporteRepository.findById(nuevaReporte.getIdReporte()).orElse(null);
        if(reporte == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Reporte no encontrado.");
        }else{
            reporte.setNombreReporte(nuevaReporte.getNombreReporte());
            reporte.setTipoReporte(nuevaReporte.getTipoReporte());
            reporte.setDescripcionReporte(nuevaReporte.getDescripcionReporte());
            reporte.setEstadoReporte(nuevaReporte.getEstadoReporte());
            
            
            return reporteRepository.save(reporte);
        }


    }
}