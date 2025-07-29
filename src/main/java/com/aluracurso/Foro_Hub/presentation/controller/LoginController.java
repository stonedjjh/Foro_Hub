package com.aluracurso.Foro_Hub.presentation.controller;


import com.aluracurso.Foro_Hub.aplication.dto.UsuarioDTO;
import com.aluracurso.Foro_Hub.aplication.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<String> validarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {

        return ResponseEntity.ok(loginService.verificarUsuario(usuarioDTO));
    }
}
