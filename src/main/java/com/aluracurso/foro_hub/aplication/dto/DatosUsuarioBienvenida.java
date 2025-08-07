package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para los datos de bienvenida de un nuevo usuario.")
public record DatosUsuarioBienvenida(
        @Schema(description = "Dirección de correo electrónico del usuario.", example = "nuevo.usuario@example.com")
        String email,
        @Schema(description = "Nombre de usuario del nuevo usuario.", example = "JuanPerez")
        String username,
        @Schema(description = "Contraseña generada automáticamente para el usuario.", example = "ContrasenaSegura123")
        String password
) {
}
