package com.aluracurso.foro_hub.presentation.controller;


import com.aluracurso.foro_hub.aplication.dto.DatosListadoUsuarioDTO;
import com.aluracurso.foro_hub.aplication.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub.aplication.dto.DatosUsuarioDTO;
import com.aluracurso.foro_hub.aplication.dto.DatosPerfilDTO;
import com.aluracurso.foro_hub.aplication.dto.UsuarioDTO;
import com.aluracurso.foro_hub.aplication.service.UsuarioService;
import com.aluracurso.foro_hub.domain.perfil.Perfil;
import com.aluracurso.foro_hub.domain.usuario.Usuario;
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

/**
 * Controlador REST para la gestión de usuarios.
 * Provee endpoints para la creación, consulta, actualización y eliminación de usuarios.
 * La documentación de la API es generada con OpenAPI (Swagger).
 */
@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuario", description = "Endpoints para la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método auxiliar para convertir una lista de entidades Perfil a una lista de DTOs.
     * @param perfiles La lista de entidades Perfil a convertir.
     * @return Una lista de DatosPerfilDTO.
     */
    private List<DatosPerfilDTO> convertirPerfilesDto(List<Perfil> perfiles){
        return perfiles.stream().map(
                p -> new DatosPerfilDTO(p.getId(), p.getNombre())).toList();
    }

    /**
     * Método auxiliar para convertir una entidad Usuario a su DTO correspondiente.
     * @param usuario La entidad Usuario a convertir.
     * @return Un objeto DatosUsuarioDTO.
     */
    private DatosUsuarioDTO convertirUsuarioDto(Usuario usuario){
        return new DatosUsuarioDTO(usuario.getId(),usuario.getNombre(),
                usuario.getCorreoElectronico(),  this.convertirPerfilesDto(usuario.getPerfiles()));
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * @param usuarioDTO El DTO con los datos del usuario a crear.
     * @return ResponseEntity con los datos del usuario creado y un estado HTTP 200 OK.
     */
    @PostMapping
    @Operation(summary = "Crea un nuevo usuario",
            description = "Este endpoint permite crear un nuevo usuario con uno o más roles asignados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente. Por favor, revisa tu correo electrónico para ver tu nombre de usuario y la contraseña temporal.",
                            content = @Content(schema = @Schema(implementation = DatosUsuarioDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "409", description = "El correo electrónico ya está registrado")
            })
    public ResponseEntity<DatosUsuarioDTO> agregarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var usuario = usuarioService.guardar(usuarioDTO);
        var datosUsuariosDTO= this.convertirUsuarioDto(usuario);
        return ResponseEntity.ok().body(datosUsuariosDTO);
    }


    /**
     * Obtiene una lista paginada de todos los usuarios.
     * Este endpoint requiere autenticación y solo puede ser accedido por usuarios con el rol 'ADMINISTRADOR'.
     * @param paginacion Objeto Pageable para controlar la paginación (tamaño, número de página, orden).
     * @return ResponseEntity con una página de DatosListadoUsuarioDTO y un estado HTTP 200 OK.
     */
    @SecurityRequirement(name = "bearer-key")
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Obtiene una lista paginada de todos los usuarios",
            description = "Este endpoint es solo accesible para usuarios con el rol ADMINISTRADOR.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no tiene el rol ADMINISTRADOR")
            })
    public ResponseEntity<Page<DatosListadoUsuarioDTO>> listarUsuarios(
            @PageableDefault(size = 10, sort = "id") Pageable paginacion) {

        var paginaDeUsuarios = usuarioService.listar(paginacion)
                .map(DatosListadoUsuarioDTO::new);

        return ResponseEntity.ok(paginaDeUsuarios);
    }

    /**
     * Busca y obtiene los detalles de un usuario por su ID.
     * Este endpoint requiere autenticación y solo puede ser accedido por un 'ADMINISTRADOR'
     * o por el propio usuario que solicita su perfil.
     * @param id El ID del usuario a buscar.
     * @return ResponseEntity con los datos del usuario encontrado y un estado HTTP 200 OK.
     */
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @Operation(summary = "Busca un usuario por su ID",
            description = "Solo un ADMINISTRADOR o el mismo usuario puede ver su perfil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                            content = @Content(schema = @Schema(implementation = DatosUsuarioDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no tiene permisos para ver este perfil"),
                    @ApiResponse(responseCode = "404", description = "No encontrado: El usuario con el ID especificado no existe")
            })
    public ResponseEntity<DatosUsuarioDTO> BuscarUsuarioId(@PathVariable Long id) {
        var usuario = usuarioService.buscar(id);
        return ResponseEntity.ok().body(this.convertirUsuarioDto(usuario));
    }

    /**
     * Elimina un usuario del sistema por su ID.
     * Este endpoint requiere autenticación y solo puede ser accedido por un 'ADMINISTRADOR'
     * o por el propio usuario que desea eliminar su cuenta.
     * @param id El ID del usuario a eliminar.
     * @return ResponseEntity sin contenido y un estado HTTP 204 No Content.
     */
    @Operation(
            summary = "Elimina un usuario por su ID",
            description = "Solo un ADMINISTRADOR o el mismo usuario puede eliminar su cuenta.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no tiene permisos para eliminar esta cuenta"),
                    @ApiResponse(responseCode = "404", description = "No encontrado: El usuario con el ID especificado no existe")
            }
    )
    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    public ResponseEntity eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza los datos de un usuario por su ID.
     * Este endpoint requiere autenticación y solo puede ser accedido por un 'ADMINISTRADOR'
     * o por el propio usuario que desea actualizar sus datos.
     * @param id El ID del usuario a actualizar.
     * @param datosActualizarUsuarioDTO El DTO con los datos para la actualización.
     * @return ResponseEntity con los datos del usuario actualizado y un estado HTTP 200 OK.
     */
    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @Operation(summary = "Actualiza los datos de un usuario",
            description = "Solo un ADMINISTRADOR o el mismo usuario puede actualizar su perfil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                            content = @Content(schema = @Schema(implementation = DatosUsuarioDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no tiene permisos para actualizar este perfil"),
                    @ApiResponse(responseCode = "404", description = "No encontrado: El usuario con el ID especificado no existe")
            }
    )
    public ResponseEntity<DatosUsuarioDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody DatosActualizarUsuarioDTO datosActualizarUsuarioDTO){
        var usuario = usuarioService.actualizar(id, datosActualizarUsuarioDTO);
        return ResponseEntity.ok().body(this.convertirUsuarioDto(usuario));
    }
}