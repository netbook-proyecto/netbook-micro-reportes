package com.example.netbook.models.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class ReporteDTO {
    private int idReporte;
    private String nombreReporte;
    private String tipoReporte;
    private String descripcionReporte;
    private String estadoReporte;
    private LocalDate fechaReporte;
}