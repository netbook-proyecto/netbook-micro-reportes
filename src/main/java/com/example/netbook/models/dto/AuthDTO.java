package com.example.netbook.models.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

// AJUSTAR: campos puestos como referencia. Cambia estos nombres
// por los que realmente devuelva el microservicio de auth (login/validate).
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AuthDTO {
    @JsonAlias({"id", "idUsuario"})
    private Integer idUsuario;

    private String username;
    private String rol;
    private String token;
}