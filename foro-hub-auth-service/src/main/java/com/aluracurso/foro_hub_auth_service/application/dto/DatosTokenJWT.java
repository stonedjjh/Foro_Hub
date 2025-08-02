package com.aluracurso.foro_hub_auth_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para el token JWT de autenticación")
public record DatosTokenJWT(
        String token
) {
}
