package com.example.netbook.models.dto;

import java.time.LocalDate;

public record ReporteDTO(
    Integer idReporte,
    String nombreReporte,
    String tipoReporte,
    String descripcionReporte,
    String estadoReporte,
    LocalDate fechaReporte
) {}