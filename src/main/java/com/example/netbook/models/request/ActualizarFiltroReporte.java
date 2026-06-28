package com.example.netbook.models.request;

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ActualizarFiltroReporte {

    @NotNull
    private int idFiltroReporte;

    @NotBlank
    private String nombreAsignatura;

    @NotNull
    private LocalDate periodoInicio; // Formato: "yyyy-MM-dd"

    @NotNull
    private LocalDate periodoFin;    // Formato: "yyyy-MM-dd"

    @NotBlank
    private String tipoReporte;

}
