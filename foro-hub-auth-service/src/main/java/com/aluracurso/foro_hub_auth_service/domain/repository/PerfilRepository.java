package com.aluracurso.foro_hub_auth_service.domain.repository;

import com.aluracurso.foro_hub_auth_service.domain.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil,Long> {
}
