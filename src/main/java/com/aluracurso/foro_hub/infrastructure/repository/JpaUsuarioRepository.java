package com.aluracurso.foro_hub.infrastructure.repository;


import com.aluracurso.foro_hub.domain.perfil.Perfil;
import com.aluracurso.foro_hub.domain.perfil.PerfilRepository;

import com.aluracurso.foro_hub.domain.usuario.Usuario;

import com.aluracurso.foro_hub.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaUsuarioRepository implements UsuarioRepository {

    private final IJpaUsuarioRepository jpaUsuarioRepository;
    private final PerfilRepository perfilRepository;

    private void mapearDominioAPersistencia(Usuario usuario, com.aluracurso.foro_hub.infrastructure.persistence.Usuario usuarioPersistencia){
        // Convierte el POJO de dominio a la entidad de persistencia
        usuarioPersistencia.setNombre(usuario.getNombre());
        usuarioPersistencia.setCorreoElectronico(usuario.getCorreoElectronico());
        usuarioPersistencia.setContraseña(usuario.getClave());

        List<com.aluracurso.foro_hub.infrastructure.persistence.Perfil> perfilesPersistencia = usuario.getPerfiles().stream()
                .map(this::convertirPerfilAPersistencia)
                .collect(Collectors.toList());

        usuarioPersistencia.setPerfiles(perfilesPersistencia);

        return;
    }

    public JpaUsuarioRepository(IJpaUsuarioRepository jpaUsuarioRepository, PerfilRepository perfilRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico) {
        return jpaUsuarioRepository.findByCorreoElectronico(correoElectronico)
                .map(this::convertirAEntidadDominio);
    }

    @Override
    public Page<Usuario> listarTodos(Pageable pageable) {
        // Delega la paginación al repositorio de JPA y convierte cada entidad a un objeto de dominio
        return jpaUsuarioRepository.findAll(pageable)
                .map(this::convertirAEntidadDominio);
    }



    @Override
    public Usuario guardar(Usuario usuarioDominio) {
        var usuarioPersistencia = new com.aluracurso.foro_hub.infrastructure.persistence.Usuario();
        this.mapearDominioAPersistencia(usuarioDominio,usuarioPersistencia);

        // Guarda en la base de datos

        var usuarioGuardado = jpaUsuarioRepository.save(usuarioPersistencia);

        // Convierte la entidad guardada de vuelta al POJO de dominio para devolver
        return convertirAEntidadDominio(usuarioGuardado);
    }

    @Override
    public Usuario actualizar(Usuario usuario) {

        var usuarioPersistencia = jpaUsuarioRepository.getById(usuario.getId());
        this.mapearDominioAPersistencia(usuario, usuarioPersistencia);
        // Guarda en la base de datos
        var usuarioGuardado = jpaUsuarioRepository.save(usuarioPersistencia);

        // Convierte la entidad guardada de vuelta al POJO de dominio para devolver
        return convertirAEntidadDominio(usuarioGuardado);
    }

    @Override
    public void eliminar(Long id) {
        this.buscarPorId(id)
                .ifPresentOrElse(
                        usuario -> jpaUsuarioRepository.deleteById(id),
                        () -> { throw new EntityNotFoundException(); }
                );
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaUsuarioRepository.findById(id).map(this::convertirAEntidadDominio);
    }

    // Método privado para la conversión de Usuario de persistencia a dominio
    public Usuario convertirAEntidadDominio(com.aluracurso.foro_hub.infrastructure.persistence.Usuario usuarioPersistencia) {
        Usuario usuarioDominio = new Usuario(
                usuarioPersistencia.getId(),
                usuarioPersistencia.getNombre(),
                usuarioPersistencia.getCorreoElectronico(),
                usuarioPersistencia.getContraseña()
        );
        usuarioDominio.setPerfiles(
                usuarioPersistencia.getPerfiles().stream()
                        .map(p -> new Perfil(p.getId(), p.getNombre()))
                        .collect(Collectors.toList())
        );
        return usuarioDominio;
    }

    // Método privado para la conversión de Perfil de dominio a persistencia
    private com.aluracurso.foro_hub.infrastructure.persistence.Perfil convertirPerfilAPersistencia(Perfil perfilDominio) {
        com.aluracurso.foro_hub.infrastructure.persistence.Perfil perfilPersistencia = new com.aluracurso.foro_hub.infrastructure.persistence.Perfil();
        perfilPersistencia.setId(perfilDominio.getId());
        perfilPersistencia.setNombre(perfilDominio.getNombre());
        return perfilPersistencia;
    }
}
