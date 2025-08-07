package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "DTO para la creación de un usuario")
public record UsuarioDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Schema(description = "Nombre completo del usuario")
        String nombre,
        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El formato del correo electrónico es inválido")
        @Schema(description = "Dirección de correo electrónico del usuario", example = "nuevo_usuario@example.com")
        String correoElectronico,
        @NotEmpty(message = "El rol es obligatorio")
        @Schema(description = "Lista de IDs de los perfiles (roles) del usuario", example = "[1, 3]")
        List<Long> idRol
) {

}