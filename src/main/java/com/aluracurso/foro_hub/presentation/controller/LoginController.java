package com.aluracurso.foro_hub.presentation.controller;


import com.aluracurso.foro_hub.aplication.dto.DatosTokenJWT;
import com.aluracurso.foro_hub.aplication.dto.LoginDTO;
import com.aluracurso.foro_hub.aplication.service.TokenGenerationService;
import com.aluracurso.foro_hub.domain.usuario.Usuario;
import com.aluracurso.foro_hub.infrastructure.security.UserDetailsFromEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticación", description = "Endpoints para la autenticación de usuarios")
public class LoginController {
    @Autowired
    private TokenGenerationService tokenGenerationService;

    @Autowired
    private AuthenticationManager manager;

    @Operation(
            summary = "Autentica a un usuario y devuelve un token JWT",
            description = "Recibe las credenciales del usuario (correo electrónico y clave) y, si son válidas, " +
                    "retorna un token JWT para acceder a los endpoints protegidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa. Retorna el token JWT.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DatosTokenJWT.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Entrada de datos Invalida",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(mediaType = "application/json")
            )
    })

    @PostMapping
    public ResponseEntity iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.correoElectronico(), loginDTO.clave());
        var autenticacion = manager.authenticate(authenticationToken);

        // Se extrae la entidad Usuario del objeto UserDetailsFromEntity.
        UserDetailsFromEntity userDetails = (UserDetailsFromEntity) autenticacion.getPrincipal();
        Usuario usuarioAutenticado = userDetails.getUsuario();

        // Se delega la generación del token al nuevo servicio.
        return ResponseEntity.ok(tokenGenerationService.generarToken(usuarioAutenticado));
    }
}