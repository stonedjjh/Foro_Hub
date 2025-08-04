package com.aluracurso.foro_hub_auth_service.presentation.controller;


import com.aluracurso.foro_hub_auth_service.application.dto.*;
import com.aluracurso.foro_hub_auth_service.application.service.UsuarioService;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "Crea un nuevo usuario",
            description = "Este endpoint permite crear un nuevo usuario con uno o más roles asignados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente",
                            content = @Content(schema = @Schema(implementation = DatosUsuarioDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "409", description = "El correo electrónico ya está registrado")
            })
    public ResponseEntity<DatosUsuarioDTO> agregarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var usuario = usuarioService.guardar(usuarioDTO);
        var datosUsuariosDTO= this.convertirUsuarioDto(usuario);
        return ResponseEntity.ok().body(datosUsuariosDTO);
    }


    @SecurityRequirement(name = "bearer-key")
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Obtiene una lista paginada de todos los usuarios",
            description = "Este endpoint es solo accesible para usuarios con el rol ADMINISTRADOR.")
    public ResponseEntity<Page<DatosListadoUsuarioDTO>> listarUsuarios(
            // PageableDefault crea una paginación por defecto de 10 elementos por página, ordenados por id.
            @PageableDefault(size = 10, sort = "id") Pageable paginacion) {

        // El servicio de usuario ya sabe cómo manejar el objeto Pageable.
        // Hacemos el mapeo de Page<Usuario> a Page<DatosListadoUsuarioDTO>
        var paginaDeUsuarios = usuarioService.listar(paginacion)
                .map(DatosListadoUsuarioDTO::new);

        return ResponseEntity.ok(paginaDeUsuarios);
    }

    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @Operation(summary = "Busca un usuario por su ID",
            description = "Solo un ADMINISTRADOR o el mismo usuario puede ver su perfil.")
    public ResponseEntity<DatosUsuarioDTO> BuscarUsuarioId(@PathVariable Long id) {
        var usuario = usuarioService.buscar(id);
        return ResponseEntity.ok().body(this.convertirUsuarioDto(usuario));
    }

    @Operation(
            summary = "Elimina un usuario por su ID",
            description = "Solo un ADMINISTRADOR o el mismo usuario puede eliminar su cuenta."
    )
    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id") // <-- Nueva validación de seguridad
    public ResponseEntity eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }



    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @Operation(summary = "Actualiza los datos de un usuario",
            description = "Solo un ADMINISTRADOR o el mismo usuario puede actualizar su perfil.")
    public ResponseEntity<DatosUsuarioDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody DatosActualizarUsuarioDTO datosActualizarUsuarioDTO){
        var usuario = usuarioService.actualizar(id, datosActualizarUsuarioDTO);
        return ResponseEntity.ok().body(this.convertirUsuarioDto(usuario));
    }

}

