package com.aluracurso.foro_hub_auth_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para crear un perfil (rol) de usuario")
public record PerfilDTO(
        @NotBlank(message = "El nombre del perfil es obligatorio")
        @Schema(description = "Nombre del perfil (ej. ADMINISTRADOR, USUARIO)")
        String nombre
) {
}
