package com.example.netbook.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity

@Data
@Table(name = "filtro_reporte")
public class FiltroReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_filtro_reporte")
    private Integer idFiltroReporte;

    // ID de asignatura (para REPORTE_BITACORA - RF07)
    @Column(nullable = false)
    private String nombreAsignatura;

    // Período de inicio del filtro
    @Column(nullable = false)
    private LocalDate periodoInicio;

    // Período de fin del filtro
    @Column(nullable = false)
    private LocalDate periodoFin;

    // Tipo de reporte al que aplica este filtro
    @Column(nullable = false)
    private String tipoReporte;

    

}