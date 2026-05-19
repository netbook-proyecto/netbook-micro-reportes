package com.example.netbook.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "filtros_reporte")
public class FiltroReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del estudiante al que aplica el filtro
    @Column(nullable = true)
    private Long idEstudiante;

    // ID del curso (para reportes grupales)
    @Column(nullable = true)
    private Long idCurso;

    // ID de asignatura (para REPORTE_BITACORA - RF07)
    @Column(nullable = true)
    private Long idAsignatura;

    // Período de inicio del filtro
    @Column(nullable = true)
    private LocalDate periodoInicio;

    // Período de fin del filtro
    @Column(nullable = true)
    private LocalDate periodoFin;

    // Tipo de reporte al que aplica este filtro
    @Column(nullable = false)
    private String tipoReporte;
}