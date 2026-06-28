package com.example.netbook.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.netbook.models.entities.FiltroReporte;
import com.example.netbook.models.request.ActualizarFiltroReporte;
import com.example.netbook.models.request.AgregarFiltroReporte;
import com.example.netbook.repositories.FiltroReporteRepository;

@Service
public class FiltroReporteService {

    private final FiltroReporteRepository filtroReporteRepository;

    public FiltroReporteService(FiltroReporteRepository filtroReporteRepository) {
        this.filtroReporteRepository = filtroReporteRepository;
    }

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
}

