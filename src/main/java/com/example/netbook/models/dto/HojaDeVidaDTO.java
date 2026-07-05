package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HojaDeVidaDTO{
    @JsonAlias({"id", "idHojaDeVida"})
    private int idHojaDeVida;
    private Integer idEstudiante;
    private String fechaAperturaExpediente;
    private String estadoExpediente;
}