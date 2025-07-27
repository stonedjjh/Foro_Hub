package com.aluracurso.Foro_Hub.domain.repository;

import com.aluracurso.Foro_Hub.domain.entity.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico,Long> {
}
