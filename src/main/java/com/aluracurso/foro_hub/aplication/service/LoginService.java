package com.aluracurso.foro_hub.aplication.service;



import com.aluracurso.foro_hub.domain.usuario.UsuarioRepository;
import com.aluracurso.foro_hub.infrastructure.security.UserDetailsFromEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Este servicio implementa la interfaz UserDetailsService de Spring Security.
 * Se encarga de buscar un usuario por su nombre de usuario (en este caso, el correo electrónico)
 * y devolver un objeto UserDetails que Spring Security pueda entender.
 */
@Service
public class LoginService implements UserDetailsService {

    // Se inyecta la interfaz del repositorio de dominio, no la implementación de JPA.
    // Esto desacopla el servicio de la capa de persistencia.
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public LoginService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga el usuario por su nombre de usuario (correo electrónico).
     * @param username El correo electrónico del usuario.
     * @return Un objeto UserDetails que representa al usuario.
     * @throws UsernameNotFoundException Si el usuario no se encuentra en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario utilizando el repositorio del dominio.
        // Se lanza una excepción si el usuario no existe.
        var usuario = usuarioRepository.buscarPorCorreoElectronico(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));

        // Se crea un objeto UserDetails a partir del POJO de dominio (Usuario),
        // utilizando la clase adaptadora UserDetailsFromEntity.
        return new UserDetailsFromEntity(usuario);
    }
}