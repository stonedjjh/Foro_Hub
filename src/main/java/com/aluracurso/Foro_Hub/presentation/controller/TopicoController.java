package com.aluracurso.Foro_Hub.presentation.controller;

import com.aluracurso.Foro_Hub.aplication.dto.TopicoDTO;
import com.aluracurso.Foro_Hub.aplication.service.TopicoApplicationService;
import com.aluracurso.Foro_Hub.domain.entity.Topico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoApplicationService topicoApplicationService;

    @GetMapping
    public String saludos() {
        return "Hello WebFlux";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> agregarNuevoTopico(@Valid @RequestBody TopicoDTO topicoDTO) {
        Topico topico = new Topico(topicoDTO);
        System.out.println(topico);
            if(topicoApplicationService.persistirRepositorio(topico))
                return ResponseEntity.status(HttpStatus.CREATED).body("Tópico Valido");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo guardar el tópico");
    }
}
