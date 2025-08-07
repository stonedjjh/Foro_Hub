package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO para mostrar los datos de un usuario")
public record DatosUsuarioDTO(
        @Schema(description = "ID único del usuario")
        Long id,
        @Schema(description = "Nombre completo del usuario")
        String nombre,
        @Schema(description = "Dirección de correo electrónico del usuario", example = "usuario@example.com")
        String correoElectronico,
        @Schema(description = "Lista de perfiles (roles) asignados al usuario")
        List<DatosPerfilDTO> permisos
) {

}
