package com.example.netbook.models.entities;

    
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;




@Data
@Entity
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDateTime fechaReporte;

    @PrePersist
    protected void onCreate() {
        this.fechaReporte = LocalDateTime.now();
    }

}
