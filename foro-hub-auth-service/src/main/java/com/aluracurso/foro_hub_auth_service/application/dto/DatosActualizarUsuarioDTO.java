package com.aluracurso.foro_hub_auth_service.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DatosActualizarUsuarioDTO(
        String nombre,
        @Email(message = "El campo debe ser una direccion de correo electronico valido")
        String correoElectronico,
        List<DatosPerfilDTO> permisos) {
}
