package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.infrastructure.persistence.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IJpaPerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByNombre(String nombreDelPerfil);
    Optional<Perfil> findById(Long Id);
}