// Archivo: DatosListadoUsuarioDTO.java
package com.aluracurso.foro_hub_auth_service.application.dto;

import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import java.util.List;
import java.util.stream.Collectors;

public record DatosListadoUsuarioDTO(
        Long id,
        String nombre,
        String correoElectronico,
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