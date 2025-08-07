package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para la información básica de un perfil o rol de usuario.")
public record DatosPerfilDTO(
        @Schema(description = "ID del perfil")
        @NotNull(message = "El id del perfil no puede ser nulo")
        Long id,
        @Schema(description = "Nombre del perfil (ej. ADMINISTRADOR, USUARIO)")
        String nombre
) {
}
