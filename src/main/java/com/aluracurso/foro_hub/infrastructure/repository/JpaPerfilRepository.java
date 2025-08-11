package com.aluracurso.foro_hub.infrastructure.repository;

import com.aluracurso.foro_hub.domain.perfil.Perfil;
import com.aluracurso.foro_hub.domain.perfil.PerfilRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Clase que implementa la interfaz de dominio PerfilRepository.
 * Utiliza la interfaz de JPA (IJpaPerfilRepository) para la persistencia.
 * Esta clase actúa como un adaptador, convirtiendo entre los objetos
 * del dominio y las entidades de persistencia.
 */
@Repository
public class JpaPerfilRepository implements PerfilRepository {

    private final IJpaPerfilRepository jpaRepository;

    public JpaPerfilRepository(IJpaPerfilRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public Optional<Perfil> encontrarPorId(Long id) {
        // Se busca la entidad de persistencia y se convierte a la entidad de dominio.
        return jpaRepository.findById(id)
                .map(this::convertirAEntidadDominio);
    }

    @Override
    public Perfil guardar(Perfil perfilDominio) {
        // Convierte el POJO de dominio a la entidad de persistencia.
        var perfilPersistencia = new com.aluracurso.foro_hub.infrastructure.persistence.Perfil();
        perfilPersistencia.setNombre(perfilDominio.getNombre());

        // Guarda la entidad en la base de datos.
        var perfilGuardado = jpaRepository.save(perfilPersistencia);

        // Convierte la entidad guardada de vuelta a un POJO de dominio.
        return convertirAEntidadDominio(perfilGuardado);
    }

    /**
     * Método de conversión de objeto de persistencia a objeto de dominio.
     * @param perfilPersistencia La entidad de persistencia.
     * @return El objeto de dominio.
     */
    public Perfil convertirAEntidadDominio(com.aluracurso.foro_hub.infrastructure.persistence.Perfil perfilPersistencia) {
        return new Perfil(
                perfilPersistencia.getId(),
                perfilPersistencia.getNombre()
        );
    }
}
