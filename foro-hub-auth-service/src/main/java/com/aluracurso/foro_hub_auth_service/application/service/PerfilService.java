package com.aluracurso.foro_hub_auth_service.application.service;

import com.aluracurso.foro_hub_auth_service.domain.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {

    @Autowired
    PerfilRepository perfilRepository;

}
