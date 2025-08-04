package com.aluracurso.foro_hub_auth_service.aplicacion.service;

import com.aluracurso.foro_hub_auth_service.dominio.perfil.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {

    @Autowired
    PerfilRepository perfilRepository;

}
