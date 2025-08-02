package com.aluracurso.foro_hub_auth_service.domain.repository;


import com.aluracurso.foro_hub_auth_service.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
}
