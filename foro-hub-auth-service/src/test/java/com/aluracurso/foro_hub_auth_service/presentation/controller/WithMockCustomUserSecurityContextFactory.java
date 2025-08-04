package com.aluracurso.foro_hub_auth_service.presentation.controller;

import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.infraestructura.security.UserDetailsFromEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Factoría para crear un SecurityContext con un principal de tipo UserDetailsFromEntity.
 * Esto permite que las pruebas que usan la anotación @WithMockCustomUser funcionen correctamente.
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Creamos un objeto Usuario con el ID y roles de la anotación
        Usuario usuario = new Usuario(annotation.id(), "Test User", annotation.username(), "password-hash");
        usuario.setPerfiles(Arrays.stream(annotation.roles())
                .map(role -> new Perfil(0L, role))
                .collect(Collectors.toList()));

        // Creamos nuestro principal personalizado con el Usuario
        UserDetailsFromEntity principal = new UserDetailsFromEntity(usuario);

        // Creamos el token de autenticación
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password-hash", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
