package com.aluracurso.foro_hub_auth_service.infraestructura.repositorio;

import com.aluracurso.foro_hub_auth_service.dominio.perfil.PerfilRepository;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.UsuarioRepository;
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

    private void mapearDominioAPersistencia(Usuario usuario, com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Usuario usuarioPersistencia){


        // Convierte el POJO de dominio a la entidad de persistencia
        usuarioPersistencia.setNombre(usuario.getNombre());
        usuarioPersistencia.setCorreoElectronico(usuario.getCorreoElectronico());
        usuarioPersistencia.setContraseña(usuario.getClave());

   /*     // Obtiene los perfiles existentes de la base de datos y los convierte a objetos de persistencia
        List<com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil> perfilesPersistencia = usuario.getPerfiles().stream()
                .map(perfilDominio -> {
                    var perfilDelDominio = perfilRepository.encontrarPorNombre(perfilDominio.getNombre())
                            .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilDominio.getNombre()));
                    return convertirPerfilAPersistencia(perfilDelDominio);
                })
                .collect(Collectors.toList());*/

        // Transforma la lista de perfiles del dominio a una lista de perfiles de persistencia
        List<com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil> perfilesPersistencia = usuario.getPerfiles().stream()
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
        var usuarioPersistencia = new com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Usuario();
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
    private Usuario convertirAEntidadDominio(com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Usuario usuarioPersistencia) {
        Usuario usuarioDominio = new Usuario(
                usuarioPersistencia.getId(),
                usuarioPersistencia.getNombre(),
                usuarioPersistencia.getCorreoElectronico(),
                usuarioPersistencia.getContraseña()
        );
        usuarioDominio.setPerfiles(
                usuarioPersistencia.getPerfiles().stream()
                        .map(p -> new com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil(p.getId(), p.getNombre()))
                        .collect(Collectors.toList())
        );
        return usuarioDominio;
    }

    // Método privado para la conversión de Perfil de dominio a persistencia
    private com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil convertirPerfilAPersistencia(com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil perfilDominio) {
        com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil perfilPersistencia = new com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil();
        perfilPersistencia.setId(perfilDominio.getId());
        perfilPersistencia.setNombre(perfilDominio.getNombre());
        return perfilPersistencia;
    }
}