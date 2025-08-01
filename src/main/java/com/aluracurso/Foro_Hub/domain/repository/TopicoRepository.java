package com.aluracurso.Foro_Hub.domain.repository;

import com.aluracurso.Foro_Hub.domain.entity.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//Importaciones necesarias para la paginación

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico,Long> {

    List<Topico> findTop10ByOrderByFechaCreacionDesc();



    //Se define un método usando JPQL para realizar las consultas mas especificas
    @Query("SELECT t FROM Topico t WHERE UPPER(t.titulo) LIKE %:nombre% AND FUNCTION('YEAR', t.fechaCreacion) = :anio")
    List<Topico> buscarTopicosPorTituloYAnio(String nombre, int anio);

}
