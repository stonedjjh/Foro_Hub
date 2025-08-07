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
        Page<Topico> topicos = topicoRepository.findAll(paginacion);
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

    @Transactional
    public Topico guardarTopico(Topico topico, long cursoEnDTO) throws TopicoDuplicadoException, CursoNoEncontradoException  {
        try {
            var curso = cursoRepository.getReferenceById(cursoEnDTO);
            topico.setCurso(curso);
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            var userDetails = (UserDetails) authentication.getPrincipal();

            // Buscar la entidad Usuario completa en la base de datos
            var usuarioAutenticado = usuarioRepository.buscarPorCorreoElectronico(userDetails.getUsername());

            if (usuarioAutenticado == null) {
                // Manejar el caso en que no se encuentre el usuario
                throw new RuntimeException("Usuario no encontrado en la base de datos.");
            }
            Long idUsuario = usuarioAutenticado.get().getId();
            topico.setAutor(idUsuario);
            topicoRepository.save(topico);
            return topico;
        } catch (DataIntegrityViolationException e) {
            throw new TopicoDuplicadoException("El título o mensaje del tópico ya existe.");
        } catch (RuntimeException e) {
            throw new CursoNoEncontradoException("El curso ingresado no existe " + e.getMessage());
        }
    }

    @Transactional
    public DatosTopicoDTO actualizarTopico(Long id, TopicoActualizacionDTO topicoActualizacionDTO) throws TopicoDuplicadoException  {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoNoEncontradoException("Tópico con ID " + id + " no encontrado."));
        try {
                topico.setTitulo(topicoActualizacionDTO.titulo());
                topico.setMensaje(topicoActualizacionDTO.mensaje());
                topico.setStatus(topicoActualizacionDTO.status());
                topicoRepository.save(topico);
        }        catch (DataIntegrityViolationException e) {
            throw new TopicoDuplicadoException("El título o mensaje del tópico ya existe.");
        }
        return new DatosTopicoDTO(topico);
    }

    @Transactional
    public void eliminarTopico(Long id){
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoNoEncontradoException("Tópico con ID " + id + " no encontrado."));
        topicoRepository.delete(topico);
    }



}
