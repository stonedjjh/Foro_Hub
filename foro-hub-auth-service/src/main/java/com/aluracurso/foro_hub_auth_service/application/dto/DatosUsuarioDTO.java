package com.aluracurso.foro_hub_auth_service.application.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DTO para mostrar los datos de un usuario")
public record DatosUsuarioDTO(
        Long id,
        String nombre,
        String correoElectronico,
        List<DatosPerfilDTO> permisos
) {

}
