package com.aluracurso.Foro_Hub.aplication.service;

import com.aluracurso.Foro_Hub.aplication.dto.DatosTopicoDTO;
import com.aluracurso.Foro_Hub.aplication.dto.TopicoActualizacionDTO;
import com.aluracurso.Foro_Hub.aplication.dto.TopicoDTO;
import com.aluracurso.Foro_Hub.domain.entity.Topico;
import com.aluracurso.Foro_Hub.domain.entity.Usuario;
import com.aluracurso.Foro_Hub.domain.exception.CursoNoEncontradoException;
import com.aluracurso.Foro_Hub.domain.exception.TopicoDuplicadoException;
import com.aluracurso.Foro_Hub.domain.exception.TopicoNoEncontradoException;
import com.aluracurso.Foro_Hub.domain.repository.CursoRepository;
import com.aluracurso.Foro_Hub.domain.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
            var curso = cursoRepository.getById(cursoEnDTO);
            topico.setCurso(curso);
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
            Long idUsuario = usuarioAutenticado.getId();
            topico.setAutor(idUsuario);
            topicoRepository.save(topico);
            return topico;
        } catch (DataIntegrityViolationException e) {
            throw new TopicoDuplicadoException("El título o mensaje del tópico ya existe.");
        } catch (RuntimeException e) {
            throw new CursoNoEncontradoException("El curso ingresado no existe ");
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
                //topico.setCurso(topicoActualizacionDTO.curso());
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
