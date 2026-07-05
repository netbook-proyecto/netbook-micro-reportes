package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class EstudianteDTO{
    @JsonAlias({"id", "idEstudiante", "id_usuario"})
    private Integer idEstudiante;
    private String nombres;
    private String apellidoPaterno;
}