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
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private int idReporte;

    @Column(nullable = false)
    private String nombreReporte;

    @Column(nullable = false)
    private String tipoReporte;
    
    @Column(nullable = false)
    private String descripcionReporte;

    @Column(nullable = false)
    private String estadoReporte;

    @Column(name = "fecha_reporte", nullable = false, updatable = false)
    private LocalDate fechaReporte;

    
    
    
    


}
