package com.aluracurso.foro_hub.aplication.service;

import com.aluracurso.foro_hub.domain.respuesta.Respuesta;
import com.aluracurso.foro_hub.domain.respuesta.RespuestaRepository; // Usamos el repositorio de dominio
import com.aluracurso.foro_hub.domain.respuesta.exception.SolucionYaMarcadaException;
import com.aluracurso.foro_hub.domain.topico.TopicoRepository;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoNoEncontradoException;
import com.aluracurso.foro_hub.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de respuestas.
 * Contiene la lógica de negocio para crear, leer, actualizar y eliminar respuestas.
 * Este servicio interactúa con las interfaces de repositorio del dominio.
 */
@Service
public class RespuestaService {

    // Repositorio de dominio para la entidad Respuesta
    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UsuarioService usuarioService;

    // Repositorios de dominio para validar la existencia de entidades relacionadas
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Guarda una nueva respuesta en la base de datos.
     * Valida que el tópico y el autor (usuario) existan antes de guardar.
     *
     * @param respuesta La entidad Respuesta a guardar.
     * @return Un Optional con la respuesta guardada, o vacío si las validaciones fallan.
     */
    public Optional<Respuesta> guardar(Respuesta respuesta) {
        // Valida que el tópico y el usuario existan antes de guardar la respuesta
        if (!topicoRepository.existsById(respuesta.getTopico().getId())) {
            throw new TopicoNoEncontradoException("No existe el tópico con el id " + respuesta.getTopico().getId());
        }


        // Por defecto, una respuesta nueva no es una solución
        respuesta.setSolucion(false);
        var usuario = usuarioService.obtenerUsuarioAutenticado();
        respuesta.setAutor(usuario);
        Optional<Respuesta> respuestaGuardada = respuestaRepository.guardar(respuesta);
        return respuestaGuardada;
    }

    /**
     * Lista todas las respuestas asociadas a un tópico específico.
     *
     * @param topicoId El ID del tópico.
     * @return Una lista de respuestas.
     */
    public List<Respuesta> listarPorTopico(Long topicoId) {
        return respuestaRepository.listarPorTopico(topicoId);
    }

    /**
     * Actualiza el mensaje de una respuesta existente.
     * Se usa `flatMap` para aplanar el Optional anidado, ya que el método `guardar`
     * del repositorio de dominio devuelve un Optional<Respuesta>.
     *
     * @param id El ID de la respuesta a actualizar.
     * @return Un Optional con la respuesta actualizada, o vacío si no se encuentra.
     */
    public Optional<Respuesta> actualizar(Long id,String mensaje) {
           return respuestaRepository.actualizar(id, mensaje);
    }

    /**
     * Marca una respuesta como la solución de un tópico.
     * Valida que el tópico no tenga ya una solución.
     * Esta operación es transaccional, asegurando que se actualice la respuesta.
     *
     * @param id El ID de la respuesta a marcar como solución.
     * @return Un Optional con la respuesta marcada como solución, o vacío si no se encuentra
     * o el tópico ya tiene una solución.
     */
    @Transactional
    public Optional<Respuesta> marcarComoSolucion(Long id) {
        return respuestaRepository.marcarComoSolucion(id);
    }

    /**
     * Elimina una respuesta por su ID.
     *
     * @param id El ID de la respuesta a eliminar.
     * @return true si la eliminación fue exitosa, false si la respuesta no se encontró.
     */
    public boolean eliminar(Long id) {
        return respuestaRepository.buscarPorId(id).map(respuesta -> {
            respuestaRepository.eliminar(respuesta.getId());
            return true;
        }).orElse(false);
    }
}
