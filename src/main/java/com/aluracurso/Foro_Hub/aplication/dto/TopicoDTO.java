package com.aluracurso.Foro_Hub.aplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TopicoDTO(
        @NotBlank(message = "El titulo no puede estar vacío")
        String titulo,
        @NotBlank(message = "El mensaje no puede estar vacío")
        String mensaje,
        @NotBlank(message = "El status no puede estar vacío")
        String status,
        @NotNull(message = "El curso no puede estar vacío")
        Long curso
) {
}
