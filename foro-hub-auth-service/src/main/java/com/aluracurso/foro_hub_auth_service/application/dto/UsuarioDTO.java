package com.aluracurso.foro_hub_auth_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para las credenciales de inicio de sesión")
public record UsuarioDTO(
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El formato del correo electrónico es inválido")
        String correoElectronico,
        @NotBlank(message = "La contraseña es obligatoria")
        String clave
) {
}

