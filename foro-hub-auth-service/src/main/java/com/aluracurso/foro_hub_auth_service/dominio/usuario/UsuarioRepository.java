// Archivo: UsuarioRepository.java
package com.aluracurso.foro_hub_auth_service.dominio.usuario;

import java.util.List;
import java.util.Optional;

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
    Optional<Usuario> encontrarPorCorreoElectronico(String correoElectronico);

    List<Usuario> listarTodos();
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