package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AnotacionDTO {

    @JsonAlias({"id", "idAnotacion"})
    private Integer idAnotacion;

    private String tipoAnotacion;
    private String descripcionHechos;
}