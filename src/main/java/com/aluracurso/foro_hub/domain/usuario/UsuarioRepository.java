package com.aluracurso.foro_hub.domain.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz de repositorio para la entidad de dominio Usuario.
 * Define el contrato para la persistencia de usuarios, desacoplándose
 * de cualquier tecnología de base de datos específica (como JPA).
 */
public interface UsuarioRepository {

    /**
     * Busca un usuario por su correo electrónico.
     * @param correoElectronico El correo electrónico del usuario a buscar.
     * @return Un Optional que contiene el usuario si se encuentra, o un Optional vacío.
     */
    Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico);

    /**
     * Busca todos los usuarios con paginación.
     * @param pageable El objeto Pageable que define la paginación (tamaño, número de página, etc.).
     * @return Un objeto Page que contiene la lista de usuarios para la página solicitada.
     */
    Page<Usuario> listarTodos(Pageable pageable);
    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     * @param usuario El objeto de dominio Usuario a guardar.
     * @return El objeto de dominio Usuario guardado, con su ID asignado.
     */
    Usuario guardar(Usuario usuario);
    Usuario actualizar(Usuario usuario);
    void eliminar(Long id);
    Optional<Usuario> buscarPorId(Long id);

}
