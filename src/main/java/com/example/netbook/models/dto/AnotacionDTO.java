package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AnotacionDTO {

    @JsonAlias({"id", "idAnotacion"})
    private Integer idAnotacion;

    private String tipoAnotacion;
    private String descripcionHechos;
}