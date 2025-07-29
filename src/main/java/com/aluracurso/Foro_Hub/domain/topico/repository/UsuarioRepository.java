package com.aluracurso.Foro_Hub.domain.topico.repository;

import com.aluracurso.Foro_Hub.domain.topico.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
