package com.aluracurso.foro_hub.aplication.service;


import com.aluracurso.foro_hub.aplication.dto.DatosTokenJWT;

import com.aluracurso.foro_hub.domain.usuario.Usuario;
import com.aluracurso.foro_hub.infrastructure.config.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenGenerationService {

    @Autowired
    private TokenService tokenService;

    public DatosTokenJWT generarToken(Usuario usuarioAutenticado) {
        var tokenJWT = tokenService.generarToken(usuarioAutenticado);
        return new DatosTokenJWT(tokenJWT);
    }
}