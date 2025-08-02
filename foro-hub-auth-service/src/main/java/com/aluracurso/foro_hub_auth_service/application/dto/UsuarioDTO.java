package com.aluracurso.foro_hub_auth_service.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        @NotBlank String correoElectronico,
        @NotBlank String clave
) {
}
