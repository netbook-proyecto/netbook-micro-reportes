package com.example.netbook.models.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;


@Data
public class HojaDeVidaDTO{
    @JsonAlias({"id", "idHojaDeVida"})
    private int idHojaDeVida;
    private String fechaAperturaExpediente;
    private String estadoExpediente;
}