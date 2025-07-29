package com.aluracurso.Foro_Hub.domain.topico.repository;

import com.aluracurso.Foro_Hub.domain.topico.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
}
