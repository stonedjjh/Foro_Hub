package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.infrastructure.persistence.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Interfaz que usa Spring Data JPA para la persistencia.
// Notar que la entidad que usa es el POJO de dominio 'Usuario'.
public interface IJpaUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

}
