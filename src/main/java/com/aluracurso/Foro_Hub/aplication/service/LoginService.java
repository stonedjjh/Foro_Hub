package com.aluracurso.Foro_Hub.aplication.service;


import com.aluracurso.Foro_Hub.aplication.dto.DatosTokenJWT;
import com.aluracurso.Foro_Hub.domain.topico.entity.Usuario;
import com.aluracurso.Foro_Hub.domain.topico.repository.UsuarioRepository;
import com.aluracurso.Foro_Hub.infrastructure.config.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Aquí es donde usamos el Optional<Usuario> que debe devolver UsuarioRepository
        // y lanzamos UsernameNotFoundException si el usuario no se encuentra.
        // Asume que tu entidad Usuario ya implementa UserDetails.
        return usuarioRepository.findByCorreoElectronico(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));
    }

    public DatosTokenJWT generarToken(Usuario usuarioAutenticado){
        // Utiliza el objeto 'usuarioAutenticado' que ya viene verificado
        var tokenJWT = tokenService.generarToken(usuarioAutenticado);
        return new DatosTokenJWT(tokenJWT);
    }
}
