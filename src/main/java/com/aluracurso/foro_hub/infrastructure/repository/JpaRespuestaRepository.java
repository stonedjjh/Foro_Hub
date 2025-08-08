package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.domain.respuesta.Respuesta;
import com.aluracurso.foro_hub.domain.respuesta.RespuestaRepository;
import org.springframework.stereotype.Repository;
import com.aluracurso.foro_hub.infrastructure.utils.GenericMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaRespuestaRepository implements RespuestaRepository {


    private final IJpaRespuestaRepository jpaRespuestaRepository;

    public JpaRespuestaRepository(IJpaRespuestaRepository jpaRespuestaRepository) {
        this.jpaRespuestaRepository = jpaRespuestaRepository;
    }


    @Override
    public Optional<Respuesta> guardar(Respuesta respuesta) {
        try {
            com.aluracurso.foro_hub.infrastructure.persistence.Respuesta respuestaPersistencia = new com.aluracurso.foro_hub.infrastructure.persistence.Respuesta();
            GenericMapper.map(respuesta, respuestaPersistencia);
            var respuestaGuardada = jpaRespuestaRepository.save(respuestaPersistencia);
            GenericMapper.map(respuestaGuardada, respuesta);
            return Optional.of(respuesta);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Respuesta> listarPorTopico(Long topicoId) {
        var respuestas = jpaRespuestaRepository.findByTopico(topicoId);
        var respuestaDominio = new Respuesta();
        if (respuestas.isEmpty())
        {
            return List.of();
        }else{
            
            var respuestasDominio = respuestas.stream()
                    .map(respuesta->GenericMapper.map(respuesta,respuestaDominio))
        }
    }

    @Override
    public void eliminar(Long respuestaId) {

    }

    @Override
    public Optional<Respuesta> actualizar(Long respuestaId, Respuesta respuesta) {
        return Optional.empty();
    }
}
