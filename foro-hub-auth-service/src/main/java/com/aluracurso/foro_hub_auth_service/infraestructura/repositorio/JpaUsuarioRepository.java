package com.aluracurso.foro_hub_auth_service.infraestructura.repositorio;

import com.aluracurso.foro_hub_auth_service.dominio.perfil.PerfilRepository;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaUsuarioRepository implements UsuarioRepository {

    private final IJpaUsuarioRepository jpaUsuarioRepository;
    private final PerfilRepository perfilRepository;

    public JpaUsuarioRepository(IJpaUsuarioRepository jpaUsuarioRepository, PerfilRepository perfilRepository) {
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Optional<Usuario> encontrarPorCorreoElectronico(String correoElectronico) {
        return jpaUsuarioRepository.findByCorreoElectronico(correoElectronico)
                .map(this::convertirAEntidadDominio);
    }

    @Override
    public Usuario guardar(Usuario usuarioDominio) {
        // Convierte el POJO de dominio a la entidad de persistencia para guardar
        com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Usuario usuarioPersistencia = new com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Usuario();
        usuarioPersistencia.setNombre(usuarioDominio.getNombre());
        usuarioPersistencia.setCorreoElectronico(usuarioDominio.getCorreoElectronico());
        usuarioPersistencia.setContraseña(usuarioDominio.getClave());

        // Obtiene los perfiles existentes de la base de datos y los convierte a objetos de persistencia
        List<com.aluracurso.foro_hub_auth_service.infraestructura.persistencia.Perfil> perfilesPersistencia = usuarioDominio.getPerfiles().stream()
                .map(perfilDominio -> {
                    var perfilDelDominio = perfilRepository.encontrarPorNombre(perfilDominio.getNombre())
                            .orElseThrow(() -> new RuntimeException("Perfil no encontrado: " + perfilDominio.getNombre()));
                    return convertirPerfilAPersistencia(perfilDelDominio);
                })
                .collect(Collectors.toList());

        usuarioPersistencia.setPerfiles(perfilesPersistencia);

        // Guarda en la base de datos
        var usuarioGuardado = jpaUsuarioRepository.save(usuarioPersistencia);

        // Convierte la entidad guardada de vuelta al POJO de dominio para devolver
        return convertirAEntidadDominio(usuarioGuardado);
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