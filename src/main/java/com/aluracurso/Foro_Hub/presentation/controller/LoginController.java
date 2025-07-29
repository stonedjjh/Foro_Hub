package com.aluracurso.Foro_Hub.presentation.controller;

import com.aluracurso.Foro_Hub.aplication.dto.UsuarioDTO;
import com.aluracurso.Foro_Hub.aplication.service.LoginService;
import com.aluracurso.Foro_Hub.domain.topico.entity.Usuario;
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
    private LoginService loginService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity iniciarSesion(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(usuarioDTO.correoElectronico(),usuarioDTO.clave());
        var autenticacion = manager.authenticate(authenticationToken);
        return ResponseEntity.ok(loginService.generarToken((Usuario) autenticacion.getPrincipal()));
    }
}
