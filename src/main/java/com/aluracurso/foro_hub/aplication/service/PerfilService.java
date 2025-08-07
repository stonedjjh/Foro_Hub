package com.aluracurso.foro_hub.aplication.service;


import com.aluracurso.foro_hub.domain.perfil.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {

    @Autowired
    PerfilRepository perfilRepository;

}
