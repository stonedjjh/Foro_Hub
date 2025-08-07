package com.aluracurso.foro_hub.aplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record TopicoActualizacionDTO(
        // Ya no se incluye el ID aquí, viene del @PathVariable
        @NotBlank(message = "El titulo no puede estar vacío")
        String titulo,
        @NotBlank(message = "El mensaje no puede estar vacío")
        String mensaje,
        @NotBlank(message = "El status no puede estar vacío")
        String status,
        @NotNull(message = "El curso no puede estar vacío")
        Integer curso
        // Fecha de creación y autor se eliminan porque no deberían ser modificables
) {
}
