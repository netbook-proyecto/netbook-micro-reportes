package com.example.netbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.netbook.models.dto.EstudianteDTO;
import com.example.netbook.models.request.AgregarEstudianteRequest;
import com.example.netbook.services.EstudianteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudianteDTO crearEstudiante(@Valid @RequestBody AgregarEstudianteRequest request) {
        return estudianteService.crearEstudiante(request);
    }
}
