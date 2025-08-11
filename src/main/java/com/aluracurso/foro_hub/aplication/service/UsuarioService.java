package com.aluracurso.foro_hub.aplication.service;

import com.aluracurso.foro_hub.aplication.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub.aplication.dto.DatosUsuarioBienvenida;
import com.aluracurso.foro_hub.aplication.dto.UsuarioDTO;
import com.aluracurso.foro_hub.domain.perfil.PerfilRepository;
import com.aluracurso.foro_hub.domain.perfil.exception.PerfilNoEncontradoException;
import com.aluracurso.foro_hub.domain.usuario.Usuario;
import com.aluracurso.foro_hub.domain.usuario.UsuarioRepository;
import com.aluracurso.foro_hub.domain.usuario.exception.CorreoElectronicoDuplicadoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
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
    // Inyecta el servicio de notificaciones directamente
    @Autowired
    private ForoNotificationService notificationService;

    // Inyección de las variables de configuración desde application.properties
    /*@Value("${microservice.notification.url}")
    private String notificationServiceUrl;

    @Value("${service.to.service.secret}")
    private String serviceToServiceSecret;*/


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
        if (existe.isPresent()) {
            throw new CorreoElectronicoDuplicadoException("El correo electrónico " + usuarioDTO.correoElectronico() + " ya está registrado");
        }

        String clave = generateRandomString(8, this.charSet);
        //System.out.println(clave);
        String claveCodificada = passwordEncoder.encode(clave);
        var roles = usuarioDTO.idRol().stream()
                .map(r -> perfilRepository.encontrarPorId(r)
                        .orElseThrow(() -> new PerfilNoEncontradoException(r.toString())))
                .toList();
        var usuario = new Usuario(usuarioDTO.nombre(),
                usuarioDTO.correoElectronico(),
                claveCodificada,
                roles);

        Usuario usuarioPersistido = usuarioRepository.guardar(usuario); // Guardar el usuario

        notificationService.sendWelcomeNotification(
                usuarioPersistido.getCorreoElectronico(),
                usuarioPersistido.getNombre(),
                clave // Importante: la clave debe ser la original, sin codificar.
        );
        return usuarioPersistido;
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

    /**
     * Obtiene el objeto completo del usuario autenticado.
     * @return El objeto Usuario del usuario autenticado.
     * @throws RuntimeException si el usuario no se encuentra en la base de datos.
     */
    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un usuario autenticado.");
        }

        // El principal puede ser un String (username) o un objeto UserDetails
        Object principal = authentication.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Buscar al usuario en la base de datos por el correo electrónico (username)
        return usuarioRepository.buscarPorCorreoElectronico(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos."));
    }

    @Override
    public String toString() {
        return "UsuarioService{" +
                "usuarioRepository=" + usuarioRepository +
                ", perfilRepository=" + perfilRepository +
                ", passwordEncoder=" + passwordEncoder +
                ", notificationService=" + notificationService +
                ", charSet='" + charSet + '\'' +
                '}';
    }
}
