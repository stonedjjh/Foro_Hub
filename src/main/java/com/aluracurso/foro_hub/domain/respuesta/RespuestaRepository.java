package com.aluracurso.foro_hub.domain.respuesta;


import java.util.List;
import java.util.Optional;

public interface RespuestaRepository {

    Optional<Respuesta> guardar(Respuesta respuesta);
    List<Respuesta> listarPorTopico(Long topicoId);
    void eliminar(Long respuestaId);
    Optional<Respuesta> actualizar(Long respuestaId, String mensaje);
    Optional<Respuesta> buscarPorId(Long id);
    Optional<Respuesta> marcarComoSolucion(Long id);
    Boolean existeSolucionParaTopico(Long topicoId);


}
