package com.aluracurso.foro_hub.infrastructure.config;


import com.aluracurso.foro_hub.aplication.dto.UserDetailsFromToken;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/login") || request.getRequestURI().startsWith("/v3/api-docs") || request.getRequestURI().startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return; // Retornar para que no se ejecute el resto del código
        }

        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            DecodedJWT decodedJWT = tokenService.verificarTokenYDecodificar(tokenJWT);

            // Extraer email (subject) y permisos directamente del token
            Long id = decodedJWT.getClaim("id").asLong();
            String subject = decodedJWT.getSubject();
            List<String> permisos = decodedJWT.getClaim("permisos").asList(String.class);

            // Crear las GrantedAuthority a partir de la lista de strings
            List<SimpleGrantedAuthority> authorities = permisos.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Crear una instancia de tu record UserDetailsFromToken
            UserDetailsFromToken principal = new UserDetailsFromToken(id, subject, authorities);

            // Crear el objeto de autenticación con la información del token
            var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

            // Establecer el objeto de autenticación en el SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null)
            return authorizationHeader.replace("Bearer ", "");
        return null;
    }
}
