package com.aluracurso.foro_hub_auth_service.presentation.controller;


import com.aluracurso.foro_hub_auth_service.application.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.application.dto.DatosPerfilDTO;
import com.aluracurso.foro_hub_auth_service.application.dto.DatosUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.application.dto.UsuarioDTO;
import com.aluracurso.foro_hub_auth_service.application.service.UsuarioService;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuario", description = "Endpoints para la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private List<DatosPerfilDTO> convertirPerfilesDto(List<Perfil> perfiles){
        return perfiles.stream().map(
                p -> new DatosPerfilDTO(p.getId(), p.getNombre())).toList();
    }

    private DatosUsuarioDTO convertirUsuarioDto(Usuario usuario){
        return new DatosUsuarioDTO(usuario.getId(),usuario.getNombre(),
                usuario.getCorreoElectronico(),  this.convertirPerfilesDto(usuario.getPerfiles()));
    }

    @PostMapping
    public ResponseEntity<DatosUsuarioDTO> agregarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var usuario = usuarioService.guardar(usuarioDTO);
        var datosUsuariosDTO= this.convertirUsuarioDto(usuario);
        return ResponseEntity.ok().body(datosUsuariosDTO);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping
    public ResponseEntity<List<DatosUsuarioDTO>> listarUsuario() {
        var usuarios = usuarioService.listar();

        var  datosUsuariosDTO = usuarios.stream().map(
            this::convertirUsuarioDto).toList();
        return ResponseEntity.ok().body(datosUsuariosDTO);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/{id}")
    public ResponseEntity<DatosUsuarioDTO> BuscarUsuarioId(@PathVariable Long id) {
            var usuario = usuarioService.buscar(id);
            return ResponseEntity.ok().body(this.convertirUsuarioDto(usuario));
    }

    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/{id}")
    public ResponseEntity<DatosUsuarioDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody DatosActualizarUsuarioDTO datosActualizarUsuarioDTO){
        var usuario = usuarioService.actualizar(id, datosActualizarUsuarioDTO);
        return ResponseEntity.ok().body(this.convertirUsuarioDto(usuario));
    }

}
