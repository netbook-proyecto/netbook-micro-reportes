package com.example.netbook.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgregarReporte {
    @NotBlank
    private String nombreReporte;
    @NotBlank
    private String tipoReporte;
    @NotBlank
    private String descripcionReporte;
    @NotBlank
    private String estadoReporte;

}
