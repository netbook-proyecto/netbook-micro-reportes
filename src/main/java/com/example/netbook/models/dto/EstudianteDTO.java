package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EstudianteDTO{
    @JsonAlias({"id", "idEstudiante", "id_usuario", "idUsuario"})
    private Integer idEstudiante;
    private String nombres;
    private String apellidoPaterno;
}