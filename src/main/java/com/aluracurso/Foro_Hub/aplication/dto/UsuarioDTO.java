package com.aluracurso.Foro_Hub.aplication.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        @NotBlank String correoElectronico,
        @NotBlank String clave
) {
}
