package com.aluracurso.foro_hub.aplication.dto;

public record EmailRequestDTO(
        String to,
        String subject,
        String name,
        String message
) {
}
