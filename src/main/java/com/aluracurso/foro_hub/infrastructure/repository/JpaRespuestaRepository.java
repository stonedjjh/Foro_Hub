package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.domain.respuesta.Respuesta;
import com.aluracurso.foro_hub.domain.respuesta.RespuestaRepository;
import com.aluracurso.foro_hub.domain.respuesta.exception.SolucionYaMarcadaException;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoNoEncontradoException;
import com.aluracurso.foro_hub.infrastructure.persistence.exception.RespuestaNoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


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
                    respuestaDominio = this.persitenciaADominio(respuestaPersistencia);
                    return respuestaDominio;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Respuesta> buscarPorId(Long id){
        com.aluracurso.foro_hub.infrastructure.persistence.Respuesta respuestaPersitencia = jpaRespuestaRepository.getReferenceById(id);
        if (respuestaPersitencia != null)
        {
            Respuesta respuesta = this.persitenciaADominio(respuestaPersitencia);
            return Optional.of(respuesta);
        }
        return Optional.empty();
    }


    @Override
    public Optional<Respuesta> marcarComoSolucion(Long id) {
        // Buscar la respuesta por su ID en la base de datos
        var respuesta = jpaRespuestaRepository.findById(id);
        if (respuesta.isEmpty()) {
            throw new RespuestaNoEncontradaException("La respuesta con id " + id +" no se encuentra.");
        }

        var respuestaEncontrada = respuesta.get();
        if(this.existeSolucionParaTopico(respuestaEncontrada.getTopico().getId())){
            throw new SolucionYaMarcadaException("La respuesta ya ha sido marcada como solución");
        }

        respuestaEncontrada.setSolucion(true);
        var respuestaGuardada = jpaRespuestaRepository.save(respuestaEncontrada);
        Respuesta respuestaDominio = this.persitenciaADominio(respuestaGuardada);
        // Devolver un Optional con el objeto de dominio actualizado
        return Optional.of(respuestaDominio);
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
        Optional<com.aluracurso.foro_hub.infrastructure.persistence.Respuesta> respuestaPersitencia = jpaRespuestaRepository.findById(respuestaId);
        if (respuestaPersitencia.isPresent()){
            jpaRespuestaRepository.delete(respuestaPersitencia.get());
        }
    }

    @Override
    public Optional<Respuesta> actualizar(Long respuestaId, String mensaje) {
        System.out.println("aqui pase");
        var respuestaPersitencia = jpaRespuestaRepository.findById(respuestaId);
        System.out.println("aqui dio error");
        if(respuestaPersitencia.isEmpty()){
            throw new RespuestaNoEncontradaException("La respuesta con id " + respuestaId +" no se encuentra.");
        }
        respuestaPersitencia.get().setMensaje(mensaje);
        var respuestaGuardada = jpaRespuestaRepository.save(respuestaPersitencia.get());
        var respuestaDominio = this.persitenciaADominio(respuestaGuardada);
        return Optional.of(respuestaDominio);
    }



}
