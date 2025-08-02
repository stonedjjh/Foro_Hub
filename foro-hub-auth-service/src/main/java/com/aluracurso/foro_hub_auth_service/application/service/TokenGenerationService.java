package com.aluracurso.foro_hub_auth_service.application.service;

import com.aluracurso.foro_hub_auth_service.application.dto.DatosTokenJWT;
import com.aluracurso.foro_hub_auth_service.domain.entity.Usuario;
import com.aluracurso.foro_hub_auth_service.infrastructure.config.TokenService;
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