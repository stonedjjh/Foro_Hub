package com.aluracurso.foro_hub_auth_service.aplicacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para el token JWT de autenticación")
public record DatosTokenJWT(
        @Schema(description = "Token JWT generado")
        String token
) {
}

