package com.example.netbook.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.netbook.models.request.ActualizarFiltroReporte;
import com.example.netbook.models.request.AgregarFiltroReporte;
import com.example.netbook.models.entities.FiltroReporte;
import com.example.netbook.repositories.FiltroReporteRepository;

@Service
public class FiltroReporteService {

    @Autowired
    private FiltroReporteRepository filtroReporteRepository;


    public List<FiltroReporte> obtenerTodoLoFiltroReporte() {
        return filtroReporteRepository.findAll();
    }

    public FiltroReporte obtenerFiltroReportePorId(int idFiltroReporte) {
        FiltroReporte filtro =  filtroReporteRepository.findById(idFiltroReporte).orElse(null);
        if (filtro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filtro no encontrado");
        }
        return filtro;
    }

    public FiltroReporte agregarFiltroReporte(AgregarFiltroReporte nuevo){
        FiltroReporte filtroreporteNuevo = new FiltroReporte();
        filtroreporteNuevo.setIdEstudiante(nuevo.getIdEstudiante());
        filtroreporteNuevo.setIdCurso(nuevo.getIdCurso());
        filtroreporteNuevo.setNombreAsignatura(nuevo.getNombreAsignatura());
        filtroreporteNuevo.setPeriodoInicio(nuevo.getPeriodoInicio());
        filtroreporteNuevo.setPeriodoFin(nuevo.getPeriodoFin());
        return filtroReporteRepository.save(filtroreporteNuevo);
    }

    public String eliminarFiltroReportePorId(int idFiltroReporte){
        FiltroReporte filtro = filtroReporteRepository.findById(idFiltroReporte).orElse(null);
        if(filtro == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Filtro no encontrado");
        } else {
            filtroReporteRepository.deleteById(idFiltroReporte);
            return "Filtro eliminado correctamente";
        }
    
    }
    public FiltroReporte actualizarFiltroReporte(ActualizarFiltroReporte nuevoFiltro){
        FiltroReporte filtroreporte = filtroReporteRepository.findById(nuevoFiltro.getIdFiltroReporte()).orElse(null);
        if(filtroreporte == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Filtro no encontrado.");
        }else{
            filtroreporte.setIdEstudiante(nuevoFiltro.getIdEstudiante());
            filtroreporte.setIdCurso(nuevoFiltro.getIdCurso());
            filtroreporte.setNombreAsignatura(nuevoFiltro.getNombreAsignatura());
            filtroreporte.setPeriodoInicio(nuevoFiltro.getPeriodoInicio());
            filtroreporte.setPeriodoFin(nuevoFiltro.getPeriodoFin());
        }
        return filtroReporteRepository.save(filtroreporte);
    }
}

