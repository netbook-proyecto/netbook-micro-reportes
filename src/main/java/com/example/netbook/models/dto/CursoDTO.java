package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CursoDTO {
    private Integer idCurso;
    private String letraCurso;
    private Integer annoAcademico;
    private String jornada;
    private Integer cuposMaximos;
}