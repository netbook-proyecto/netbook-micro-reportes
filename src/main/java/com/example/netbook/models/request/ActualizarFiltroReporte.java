package com.example.netbook.models.request;

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

@Data
public class ActualizarFiltroReporte {

    @NotBlank
    private int idFiltroReporte;

    @NotBlank
    private int idEstudiante;

    @NotBlank
    private int idCurso;

    @NotBlank
    private String nombreAsignatura;

    @NotBlank
    private LocalDate periodoInicio; // Formato: "yyyy-MM-dd"

    @NotBlank
    private LocalDate periodoFin;    // Formato: "yyyy-MM-dd"

    @NotBlank
    private String tipoReporte;

}
