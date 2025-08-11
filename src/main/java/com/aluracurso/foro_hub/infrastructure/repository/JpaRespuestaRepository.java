package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.domain.respuesta.Respuesta;
import com.aluracurso.foro_hub.domain.respuesta.RespuestaRepository;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoNoEncontradoException;
import com.aluracurso.foro_hub.domain.usuario.Usuario;
import com.aluracurso.foro_hub.infrastructure.persistence.Topico;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.aluracurso.foro_hub.infrastructure.utils.GenericMapper;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaRespuestaRepository implements RespuestaRepository {


    private final IJpaRespuestaRepository jpaRespuestaRepository;

    @Autowired
    private IJpaTopicoRepository iJpaTopicoRepository;

    @Autowired
    private IJpaUsuarioRepository iJpaUsuarioRepository;

    @Autowired
    private JpaUsuarioRepository jpaUsuarioRepository;

    @Autowired
    private JpaTopicoRepository jpaTopicoRepository;

    public JpaRespuestaRepository(IJpaRespuestaRepository jpaRespuestaRepository) {
        this.jpaRespuestaRepository = jpaRespuestaRepository;
    }

    private com.aluracurso.foro_hub.infrastructure.persistence.Respuesta dominioAPersistencia(Respuesta respuesta){
        var respuestaPersistencia = new com.aluracurso.foro_hub.infrastructure.persistence.Respuesta();
        var usuario = iJpaUsuarioRepository.findById(respuesta.getAutor().getId());

                        //.orElseThrow(() -> new NotFound("El usuario no se encuentra registrado"));
        var topico  = iJpaTopicoRepository.findById(respuesta.getTopico().getId())
                        .orElseThrow(() -> new TopicoNoEncontradoException("No se encontro el tópico"));

        respuestaPersistencia.setMensaje(respuesta.getMensaje());
        respuestaPersistencia.setAutor(usuario.get());
        respuestaPersistencia.setTopico(topico);
        respuestaPersistencia.setSolucion(Boolean.FALSE);
        return respuestaPersistencia;
    }

    @Override
    public Optional<Respuesta> guardar(Respuesta respuesta) {
        var respuestaPersistencia = this.dominioAPersistencia(respuesta);
        var respuestaGuardada = jpaRespuestaRepository.save(respuestaPersistencia);
        var respuestaDominio = this.persitenciaADominio(respuestaGuardada);

        return Optional.of(respuestaDominio);
    }

    private Respuesta persitenciaADominio(com.aluracurso.foro_hub.infrastructure.persistence.Respuesta respuestaGuardada) {
        var respuestaDominio = new Respuesta();
        System.out.println("Autor respuesta guardada" + respuestaGuardada.getAutor());
        System.out.println("Topico respuesta guardada" + respuestaGuardada.getTopico());
        respuestaDominio.setId(respuestaGuardada.getId());
        respuestaDominio.setAutor(jpaUsuarioRepository.convertirAEntidadDominio(respuestaGuardada.getAutor()));
        respuestaDominio.setTopico(jpaTopicoRepository.persistenciaADominio(respuestaGuardada.getTopico()));
        respuestaDominio.setFechaCreacion(respuestaGuardada.getFechaCreacion());
        respuestaDominio.setSolucion(respuestaGuardada.getSolucion());
        respuestaDominio.setMensaje(respuestaGuardada.getMensaje());
        return respuestaDominio;
    }

    @Override
    public List<Respuesta> listarPorTopico(Long topicoId) {
        // Obtener las respuestas de la capa de persistencia
        var respuestasPersistencia = jpaRespuestaRepository.findByTopicoId(topicoId);

        // Mapear cada objeto de persistencia a un nuevo objeto de dominio
        return respuestasPersistencia.stream()
                .map(respuestaPersistencia -> {
                    Respuesta respuestaDominio = new Respuesta();
                    GenericMapper.map(respuestaPersistencia, respuestaDominio);
                    return respuestaDominio;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Respuesta> buscarPorId(Long id){
        com.aluracurso.foro_hub.infrastructure.persistence.Respuesta respuestaPersitencia = jpaRespuestaRepository.getReferenceById(id);
        if (respuestaPersitencia != null)
        {
            Respuesta respuesta = new Respuesta();
            GenericMapper.map(respuestaPersitencia,respuesta);
            return Optional.of(respuesta);
        }
        return Optional.empty();
    }


    @Override
    public Optional<Respuesta> marcarComoSolucion(Long id) {
        // Buscar la respuesta por su ID en la base de datos
        return jpaRespuestaRepository.findById(id).map(respuestaPersistencia -> {
            // Marcar el campo 'solucion' como true
            respuestaPersistencia.setSolucion(true);
            // Guardar la entidad actualizada en la base de datos
            var respuestaGuardada = jpaRespuestaRepository.save(respuestaPersistencia);
            // Mapear la entidad guardada de vuelta al objeto de dominio
            Respuesta respuestaDominio = new Respuesta();
            GenericMapper.map(respuestaGuardada, respuestaDominio);
            // Devolver un Optional con el objeto de dominio actualizado
            return respuestaDominio;
        });
    }

    @Override
    public Boolean existeSolucionParaTopico(Long topicoId) {
        Optional<com.aluracurso.foro_hub.infrastructure.persistence.Respuesta> respuestaPersitencia =
                jpaRespuestaRepository.buscarSolucionTopico(topicoId);
        if (respuestaPersitencia.isPresent())
        {
            return true;
        }
        return false;
    }

    @Override
    public void eliminar(Long respuestaId) {
        com.aluracurso.foro_hub.infrastructure.persistence.Respuesta respuestaPersitencia = jpaRespuestaRepository.getReferenceById(respuestaId);
        if (respuestaPersitencia != null){
            jpaRespuestaRepository.delete(respuestaPersitencia);
        }
    }

    @Override
    public Optional<Respuesta> actualizar(Long respuestaId, Respuesta respuesta) {
        com.aluracurso.foro_hub.infrastructure.persistence.Respuesta respuestaPersitencia = jpaRespuestaRepository.getReferenceById(respuestaId);
        if(respuesta.getMensaje()!=null){
            respuestaPersitencia.setMensaje(respuesta.getMensaje());
        }
        var respuestaGuardada = jpaRespuestaRepository.save(respuestaPersitencia);
        GenericMapper.map(respuestaGuardada, respuesta);
        return Optional.of(respuesta);
    }
}
