package com.aluracurso.foro_hub.aplication.service;

import com.aluracurso.foro_hub.aplication.dto.DatosTopicoDTO;
import com.aluracurso.foro_hub.aplication.dto.TopicoActualizacionDTO;
import com.aluracurso.foro_hub.domain.topico.Topico;
import com.aluracurso.foro_hub.domain.topico.TopicoRepository;
import com.aluracurso.foro_hub.domain.curso.exception.CursoNoEncontradoException;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoDuplicadoException;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoNoEncontradoException;
import com.aluracurso.foro_hub.domain.curso.CursoRepository;
import com.aluracurso.foro_hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class TopicoApplicationService {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Se inyecta el nuevo servicio de notificaciones
    @Autowired
    private NotificationService notificationService;

    private Optional<List<DatosTopicoDTO>> convertirDTO(List<Topico> listaTopico){
        if(!listaTopico.isEmpty())
            return Optional.of(listaTopico.stream().map(
                            DatosTopicoDTO::new
                    )
                    .toList());
        return Optional.empty();
    }

    private Page<DatosTopicoDTO> convertirDTO(Page<Topico> paginaTopicos){
        // Mapea cada Topico en la página a DatosTopicoDTO
        return paginaTopicos.map(DatosTopicoDTO::new);
    }

    public Page<DatosTopicoDTO> obtenerTopicos(Pageable paginacion){
        // Llama a findAll del repositorio, que ahora acepta Pageable y devuelve Page<Topico>
        Page<Topico> topicos = topicoRepository.buscarTodos(paginacion);
        // Convierte la Page de Topico a Page de DatosTopicoDTO
        return convertirDTO(topicos);
    }

    public DatosTopicoDTO obtenerTopicoPorId(Long id){
        Optional<Topico> topico= topicoRepository.findById(id);
        try {
            return new DatosTopicoDTO(topico.get());
        } catch (RuntimeException e) {
            throw new TopicoNoEncontradoException("Tópico con ID " + id + " no encontrado.");
        }
    }



    public Optional<List<DatosTopicoDTO>> obtener10Topicos(){
        return convertirDTO(topicoRepository.findTop10ByOrderByFechaCreacionDesc());
    }

    public Optional<List<DatosTopicoDTO>> buscarTituloyAnio(String titulo, Integer anio)
    {
        return convertirDTO(topicoRepository.buscarTopicosPorTituloYAnio(titulo,anio));
    }

    /**
     * Guarda un nuevo tópico, asignando el curso y el autor autenticado.
     * @param topico El objeto Tópico a guardar.     *
     * @return El tópico guardado.
     * @throws TopicoDuplicadoException si el título o mensaje ya existen.
     * @throws CursoNoEncontradoException si el curso no se encuentra.
     */
    @Transactional
    public Topico guardarTopico(Topico topico) throws TopicoDuplicadoException, CursoNoEncontradoException {
        try {
            // Lógica de negocio y orquestación
            var curso = cursoRepository.findById(topico.getCurso())
                    .orElseThrow(() -> new CursoNoEncontradoException("El curso ingresado no existe."));

            var authentication = SecurityContextHolder.getContext().getAuthentication();
            var userDetails = (UserDetails) authentication.getPrincipal();
            var usuario = usuarioRepository.buscarPorCorreoElectronico(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos."));

            topico.setAutor(usuario.getId());
            // Persistencia
            Optional<Topico> topicoGuardado = topicoRepository.guardarTopico(topico);

            // Llama al servicio de notificaciones después de guardar
            notificationService.notifyNewTopic(topicoGuardado.get());

            return topicoGuardado.get();
        } catch (DataIntegrityViolationException e) {
            throw new TopicoDuplicadoException("El título o mensaje del tópico ya existe.");
        }
    }

    /**
     * Actualiza un tópico existente. La lógica de actualización reside en la clase de dominio Topico.
     * @param id El ID del tópico a actualizar.
     * @param topicoActualizacionDTO Los datos para la actualización.
     * @return Un DTO con los datos del tópico actualizado.
     * @throws TopicoNoEncontradoException si el tópico no se encuentra.
     * @throws TopicoDuplicadoException si la actualización genera un tópico duplicado.
     */
    @Transactional
    public DatosTopicoDTO actualizarTopico(Long id, TopicoActualizacionDTO topicoActualizacionDTO) throws TopicoDuplicadoException {
        try {
            // Orquestación: busca el tópico y le pide que se actualice a sí mismo.
            Topico topico = topicoRepository.findById(id)
                    .orElseThrow(() -> new TopicoNoEncontradoException("Tópico con ID " + id + " no encontrado."));

            if (!topicoActualizacionDTO.titulo().isEmpty()){
                topico.setTitulo(topicoActualizacionDTO.titulo());
            }

            if (!topicoActualizacionDTO.mensaje().isEmpty()){
                topico.setMensaje(topicoActualizacionDTO.mensaje());
            }

            if (!topicoActualizacionDTO.status().isEmpty()){
                topico.setStatus(topicoActualizacionDTO.status());
            }

            // Persistencia: guarda el tópico modificado
            var topicoActualizado = topicoRepository.actualizarTopico(topico);

            return new DatosTopicoDTO(topicoActualizado.get());
        } catch (DataIntegrityViolationException e) {
            throw new TopicoDuplicadoException("El título o mensaje del tópico ya existe.");
        }
    }

    /**
     * Elimina un tópico por su ID.
     * @param id El ID del tópico a eliminar.
     * @throws TopicoNoEncontradoException si el tópico no se encuentra.
     */
    @Transactional
    public void eliminarTopico(Long id) {
        // Orquestación y Persistencia
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoNoEncontradoException("Tópico con ID " + id + " no encontrado."));

        topicoRepository.eliminarTopico(topico);
    }
}
