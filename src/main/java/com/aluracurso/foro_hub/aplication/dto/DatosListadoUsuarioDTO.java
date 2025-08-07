package com.aluracurso.foro_hub.aplication.dto;


import com.aluracurso.foro_hub.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "DTO para listar los usuarios con sus perfiles.")
public record DatosListadoUsuarioDTO(
        @Schema(description = "ID del usuario")
        Long id,
        @Schema(description = "Nombre completo del usuario")
        String nombre,
        @Schema(description = "Dirección de correo electrónico del usuario")
        String correoElectronico,
        @Schema(description = "Lista de roles (perfiles) asignados al usuario")
        List<String> perfiles
) {
    public DatosListadoUsuarioDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getPerfiles().stream()
                        .map(p -> p.getNombre())
                        .collect(Collectors.toList())
        );
    }
}