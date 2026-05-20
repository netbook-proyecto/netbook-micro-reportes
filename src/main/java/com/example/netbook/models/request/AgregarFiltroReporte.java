package com.example.netbook.models.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarFiltroReporte {
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
