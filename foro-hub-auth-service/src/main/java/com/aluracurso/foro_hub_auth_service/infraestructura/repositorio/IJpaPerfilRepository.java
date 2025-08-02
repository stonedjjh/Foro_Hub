package com.aluracurso.foro_hub_auth_service.infraestructura.repositorio;



import com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IJpaPerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByNombre(String nombreDelPerfil);
}
