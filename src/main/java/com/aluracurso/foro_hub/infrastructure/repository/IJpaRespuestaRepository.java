package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.infrastructure.persistence.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IJpaRespuestaRepository extends JpaRepository<Respuesta,Long> {
    List<Respuesta> findByTopico(Long topico);

}
