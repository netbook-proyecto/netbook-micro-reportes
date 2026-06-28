package com.example.netbook.models.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

public record MensajeriaDTO(
    // Con @JsonAlias permites que acepte tanto "id" (el de tu compañero) como "idMensaje" (el tuyo)
    @JsonAlias({"id", "idMensaje"})
    Integer idMensaje,
    
    LocalDate fechaEnvio,
    String asunto
) {}