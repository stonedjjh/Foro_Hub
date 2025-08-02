package com.aluracurso.Foro_Hub.aplication.service;

import com.aluracurso.Foro_Hub.domain.entity.Perfil;
import com.aluracurso.Foro_Hub.domain.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerfilService {

    @Autowired
    PerfilRepository perfilRepository;

}
