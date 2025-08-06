package com.aluracurso.foro_hub_auth_service.aplicacion.service;

import com.aluracurso.foro_hub_auth_service.aplicacion.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.aplicacion.dto.DatosUsuarioBienvenida;
import com.aluracurso.foro_hub_auth_service.aplicacion.dto.UsuarioDTO;
import com.aluracurso.foro_hub_auth_service.dominio.exceptions.CorreoElectronicoDuplicadoException;
import com.aluracurso.foro_hub_auth_service.dominio.exceptions.PerfilNoEncontradoException;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.PerfilRepository;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.UsuarioRepository;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page; // Importado para la paginación
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient; // Importar WebClient
import reactor.core.publisher.Mono; // Importar Mono


import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String generateRandomString(int length, String charSet) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            sb.append(charSet.charAt(randomIndex));
        }
        return sb.toString();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Genera una contraseña aleatoria y la envía al servicio de notificaciones.
     * @param usuarioDTO DTO con los datos del nuevo usuario.
     * @return El usuario guardado.
     */
    public Usuario guardar(UsuarioDTO usuarioDTO) {
        var existe = this.buscarPorCorreoElectronico(usuarioDTO.correoElectronico());
        if(existe.isPresent()){
            throw new CorreoElectronicoDuplicadoException("El correo electrónico " + usuarioDTO.correoElectronico() + " ya esta registrado");
        }

            String clave = generateRandomString(8, this.charSet);
            System.out.println(clave);
            clave=passwordEncoder.encode(clave);
            var roles=usuarioDTO.idRol().stream()
                    .map(r-> {
                        return perfilRepository.encontrarPorId(r)
                                .orElseThrow(() ->new PerfilNoEncontradoException(r.toString()));
                        }).toList();
            var usuario = new Usuario(usuarioDTO.nombre(),
                    usuarioDTO.correoElectronico(),
                    clave,
                    roles);

        Usuario usuarioPersistido = usuarioRepository.guardar(usuario); // Guardar el usuario

        // Enviar notificación de bienvenida al microservicio de notificaciones
        sendWelcomeNotificationToMicroservice(usuarioPersistido.getCorreoElectronico(), usuarioPersistido.getNombre(), clave); // Enviar la contraseña SIN codificar

        return usuarioPersistido;
    }

    /**
     * Realiza la llamada HTTP al microservicio de notificaciones para enviar el correo de bienvenida.
     * @param email Correo del usuario.
     * @param username Nombre del usuario.
     * @param password Contraseña generada (sin codificar).
     */
    private void sendWelcomeNotificationToMicroservice(String email, String username, String password) {
        // Objeto DTO para la petición al microservicio de notificaciones
        DatosUsuarioBienvenida datosBienvenida = new DatosUsuarioBienvenida(email, username, password);

        WebClient client = WebClient.create(notificationServiceUrl);

        client.post()
                .uri("/notificaciones/bienvenida") // Endpoint del microservicio de notificaciones
                .contentType(MediaType.APPLICATION_JSON)
                // Añadir la cabecera X-Service-Token con el secreto compartido
                .header("X-Service-Token", serviceToServiceSecret)
                .body(Mono.just(datosBienvenida), DatosUsuarioBienvenida.class)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        response -> System.out.println("Notificación de bienvenida enviada al microservicio."),
                        error -> System.err.println("Error al enviar notificación de bienvenida: " + error.getMessage())
                );
    }

    public void eliminar(Long id){
        usuarioRepository.eliminar(id);
        return;
    }

    public Usuario actualizar(Long id, DatosActualizarUsuarioDTO datosActualizarUsuarioDTO){
        var usuario = this.buscar(id);
        if(usuario!=null)
        {
            Optional.ofNullable(datosActualizarUsuarioDTO.nombre())
                    .ifPresent(usuario::setNombre);
            Optional.ofNullable(datosActualizarUsuarioDTO.correoElectronico())
                    .ifPresent(usuario::setCorreoElectronico);

            if (datosActualizarUsuarioDTO.permisos()!=null && !datosActualizarUsuarioDTO.permisos().isEmpty())
            {
                usuario.setPerfiles(datosActualizarUsuarioDTO.permisos()
                        .stream()
                        .map(
                        permiso-> perfilRepository.encontrarPorId(permiso.id()).orElseThrow(
                                ()-> new PerfilNoEncontradoException(permiso.nombre())
                        ))
                        .toList()
                );
            }

        }else{
            throw new EntityNotFoundException();
        }

        return usuarioRepository.actualizar(usuario);
    }

    public Usuario buscar(Long id){
        return usuarioRepository.buscarPorId(id).
                orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con el ID: " + id));
    }

    public Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico){
            return usuarioRepository.buscarPorCorreoElectronico(correoElectronico);
    }

    // Método 'listar' actualizado para usar paginación
    public Page<Usuario> listar(Pageable pageable){
        // Usamos el método findAll de Spring Data JPA, que ya soporta paginación
        // Tu JpaUsuarioRepository debe extender JpaRepository<Usuario, Long>
        return usuarioRepository.listarTodos(pageable);
    }
}
