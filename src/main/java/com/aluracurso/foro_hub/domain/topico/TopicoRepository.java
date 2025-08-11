package com.aluracurso.foro_hub.domain.topico;

import com.aluracurso.foro_hub.domain.curso.Curso;
import com.aluracurso.foro_hub.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

//Importaciones necesarias para la paginación

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicoRepository {

    List<Topico> findTop10ByOrderByFechaCreacionDesc();
    List<Topico> buscarTopicosPorTituloYAnio(String nombre, int anio);
    Boolean existsById(Long id);
    Optional<Topico> findById(Long id);
    Page<Topico> buscarTodos(Pageable paginacion);
    Optional<Topico> guardarTopico(Topico topico);
    Optional<Topico> actualizarTopico(Topico topico);
    void eliminarTopico(Topico topico);

}
