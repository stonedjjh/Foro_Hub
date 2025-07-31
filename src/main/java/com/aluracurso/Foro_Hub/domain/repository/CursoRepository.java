package com.aluracurso.Foro_Hub.domain.repository;

import com.aluracurso.Foro_Hub.domain.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Long> {
}
