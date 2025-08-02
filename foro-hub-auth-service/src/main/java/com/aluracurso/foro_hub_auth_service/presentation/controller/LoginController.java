package com.aluracurso.foro_hub_auth_service.presentation.controller;

import com.aluracurso.foro_hub_auth_service.application.dto.UsuarioDTO;
import com.aluracurso.foro_hub_auth_service.application.service.TokenGenerationService;
import com.aluracurso.foro_hub_auth_service.domain.entity.Usuario;
import com.aluracurso.foro_hub_auth_service.infrastructure.security.UserDetailsFromEntity; // Importación necesaria
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
public class LoginController {
    @Autowired
    private TokenGenerationService tokenGenerationService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity iniciarSesion(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.correoElectronico(), usuarioDTO.clave());
        var autenticacion = manager.authenticate(authenticationToken);

        // Se extrae la entidad Usuario del objeto UserDetailsFromEntity.
        UserDetailsFromEntity userDetails = (UserDetailsFromEntity) autenticacion.getPrincipal();
        Usuario usuarioAutenticado = userDetails.getUsuario();

        // Se delega la generación del token al nuevo servicio.
        return ResponseEntity.ok(tokenGenerationService.generarToken(usuarioAutenticado));
    }
}
