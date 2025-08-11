package com.aluracurso.foro_hub.infrastructure.repository;

import com.aluracurso.foro_hub.domain.curso.Curso;
import com.aluracurso.foro_hub.domain.curso.CursoRepository;
import com.aluracurso.foro_hub.domain.topico.Topico;
import com.aluracurso.foro_hub.domain.topico.TopicoRepository;
import com.aluracurso.foro_hub.infrastructure.persistence.Usuario;
import com.aluracurso.foro_hub.infrastructure.utils.GenericMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del TopicoRepository de dominio que utiliza Spring Data JPA
 * para la persistencia de datos.
 * Esta clase se encarga de mapear las entidades de persistencia a entidades de dominio
 * y viceversa, manteniendo la capa de dominio independiente de la tecnología de persistencia.
 */
@Repository
public class JpaTopicoRepository implements TopicoRepository {

    private final IJpaTopicoRepository jpaTopicoRepository;

    public JpaTopicoRepository(IJpaTopicoRepository jpaTopicoRepository) {
        this.jpaTopicoRepository = jpaTopicoRepository;
    }
    @Autowired
    private JpaUsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Mapea una lista de entidades de persistencia a una lista de entidades de dominio.
     *
     * @param lista La lista de entidades de persistencia.
     * @return Una lista de entidades de dominio.
     */
    private List<Topico> listaPersistidaADominio(List<com.aluracurso.foro_hub.infrastructure.persistence.Topico> lista) {
        return lista.stream()
                .map(this::persistenciaADominio)
                .collect(Collectors.toList());
    }

    /**
     * Mapea una entidad de persistencia a una entidad de dominio.
     *
     * @param topicoRepositorio La entidad de persistencia.
     * @return La entidad de dominio.
     */
    public Topico persistenciaADominio(com.aluracurso.foro_hub.infrastructure.persistence.Topico topicoRepositorio) {
        var topico = new Topico(topicoRepositorio.getId(),
                topicoRepositorio.getTitulo(),
                topicoRepositorio.getMensaje(),
                topicoRepositorio.getFechaCreacion(),
                topicoRepositorio.getStatus(),
                topicoRepositorio.getAutor().getId(),
                topicoRepositorio.getCurso().getId()
                );
        return topico;
    }

    /**
     * Mapea una entidad de dominio a una entidad de persistencia.
     *
     * @param topico La entidad de dominio.
     * @return La entidad de persistencia.
     */
    public com.aluracurso.foro_hub.infrastructure.persistence.Topico dominioAPersistencia(Topico topico) {
        var topicoRepositorio = new com.aluracurso.foro_hub.infrastructure.persistence.Topico();

        // 1. Obtenemos el ID del autor y del curso del objeto de dominio
        Long autorId = topico.getAutor();
        Long cursoId = topico.getCurso();

        // 2. Buscamos las entidades de persistencia en la base de datos
        // Usamos orElseThrow para manejar el caso en que el ID no exista
        var autor = usuarioRepository.buscarPorId(autorId)
                .orElseThrow(() -> new IllegalArgumentException("Autor con ID " + autorId + " no encontrado"));

        var autorPersitencia = new Usuario();
        GenericMapper.map(autor,autorPersitencia);

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso con ID " + cursoId + " no encontrado"));

        // 3. Asignamos los objetos de persistencia encontrados

        topicoRepositorio.setAutor(autorPersitencia);
        topicoRepositorio.setCurso(curso);

        // 4. Mapeamos el resto de los campos
        topicoRepositorio.setFechaCreacion(topico.getFechaCreacion());
        topicoRepositorio.setMensaje(topico.getMensaje());
        topicoRepositorio.setStatus(topico.getStatus());
        topicoRepositorio.setTitulo(topico.getTitulo());

        return topicoRepositorio;
    }
    @Override
    public List<Topico> findTop10ByOrderByFechaCreacionDesc() {
        var topicoPersistencia = jpaTopicoRepository.findTop10ByOrderByFechaCreacionDesc();
        return listaPersistidaADominio(topicoPersistencia);
    }

    @Override
    public List<Topico> buscarTopicosPorTituloYAnio(String nombre, int anio) {
        var topicoPersistencia = jpaTopicoRepository.buscarTopicosPorTituloYAnio(nombre, anio);
        return listaPersistidaADominio(topicoPersistencia);
    }

    /**
     * Verifica la existencia de un tópico por su ID de manera más concisa.
     *
     * @param id El ID del tópico.
     * @return true si el tópico existe, false en caso contrario.
     */
    @Override
    public Boolean existsById(Long id) {
        return jpaTopicoRepository.existsById(id);
    }

    /**
     * Busca un tópico por su ID y lo mapea al dominio de forma segura.
     * Utiliza Optional.map para evitar NullPointerException.
     *
     * @param id El ID del tópico.
     * @return Un Optional que puede contener la entidad de dominio.
     */
    @Override
    public Optional<Topico> findById(Long id) {
        return jpaTopicoRepository.findById(id)
                .map(this::persistenciaADominio);
    }

    @Override
    public Page<Topico> buscarTodos(Pageable paginacion) {
        Page<com.aluracurso.foro_hub.infrastructure.persistence.Topico> topicoPersistenciaPagina = jpaTopicoRepository.findAll(paginacion);
        return topicoPersistenciaPagina.map(this::persistenciaADominio);
    }

    @Override
    public Optional<Topico> guardarTopico(Topico topico) {
        var topicoRepositorio = this.dominioAPersistencia(topico);
        var topicoGuardado = jpaTopicoRepository.save(topicoRepositorio);
        return Optional.of(this.persistenciaADominio(topicoGuardado));
    }

    /**
     * Actualiza un tópico existente. La lógica de negocio ya debe haber ocurrido
     * en el dominio, aquí solo se persiste el objeto.
     *
     * @param topico El objeto Tópico con los datos actualizados.     *
     * @return Un Optional que contiene el tópico actualizado.
     */
    @Override
    public Optional<Topico> actualizarTopico(Topico topico) {
        // En esta implementación, el objeto topico ya debería tener el ID
        // y los datos actualizados desde la capa de servicio.
        var topicoRepositorio = dominioAPersistencia(topico);
        var topicoActualizado = jpaTopicoRepository.save(topicoRepositorio);
        return Optional.of(persistenciaADominio(topicoActualizado));
    }

    @Override
    public void eliminarTopico(Topico topico) {
        jpaTopicoRepository.delete(this.dominioAPersistencia(topico));
    }
}
