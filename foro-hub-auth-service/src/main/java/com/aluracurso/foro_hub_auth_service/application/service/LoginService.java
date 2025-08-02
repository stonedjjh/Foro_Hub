package com.aluracurso.foro_hub_auth_service.application.service;

import com.aluracurso.foro_hub_auth_service.domain.repository.UsuarioRepository;
import com.aluracurso.foro_hub_auth_service.infrastructure.security.UserDetailsFromEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByCorreoElectronico(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));

        // Se crea un objeto UserDetails a partir de la entidad Usuario
        return new UserDetailsFromEntity(usuario);
    }
}