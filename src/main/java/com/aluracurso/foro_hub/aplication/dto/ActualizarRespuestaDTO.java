package com.aluracurso.foro_hub.aplication.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para actualizar el contenido de una respuesta existente.
 *
 * @param mensaje El nuevo mensaje para la respuesta.
 */
public record ActualizarRespuestaDTO(
        @NotBlank
        String mensaje) {
}
