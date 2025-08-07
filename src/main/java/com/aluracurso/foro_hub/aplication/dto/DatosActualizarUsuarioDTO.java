package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;


import java.util.List;

@Schema(description = "DTO para actualizar los datos de un usuario existente. Los campos son opcionales para permitir actualizaciones parciales.")
public record DatosActualizarUsuarioDTO(
        @Schema(description = "Nombre completo del usuario")
        String nombre,
        @Schema(description = "Dirección de correo electrónico válida", example = "correo.nuevo@example.com")
        @Email(message = "El campo debe ser una dirección de correo electrónico válida")
        String correoElectronico,
        @Schema(description = "Lista de permisos (roles) del usuario")
        List<DatosPerfilDTO> permisos) {
}
