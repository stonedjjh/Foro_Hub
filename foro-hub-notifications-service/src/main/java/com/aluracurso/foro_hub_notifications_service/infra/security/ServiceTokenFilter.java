package com.aluracurso.foro_hub_notifications_service.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de seguridad para validar el token de servicio a servicio.
 * Este filtro intercepta las peticiones a los endpoints internos
 * y verifica la presencia y validez de la cabecera 'X-Service-Token'.
 */
@Component
public class ServiceTokenFilter extends OncePerRequestFilter {

    // Inyecta el secreto compartido desde application.properties
    @Value("${service.to.service.secret}")
    private String serviceToServiceSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Definimos qué rutas deben ser protegidas por este filtro.
        // En este caso, todas las rutas que empiezan con /notificaciones/
        // Si tienes rutas públicas en este microservicio, deberías excluirlas aquí.
        if (request.getRequestURI().startsWith("/notificaciones/")) {
            String serviceToken = request.getHeader("X-Service-Token");

            // Si el token no está presente o no coincide con el secreto configurado
            if (serviceToken == null || !serviceToken.equals(serviceToServiceSecret)) {
                // Denegar el acceso y enviar una respuesta 401 Unauthorized
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Acceso no autorizado: Token de servicio inválido o ausente.\"}");
                return; // Detener la cadena de filtros
            }
        }
        // Si el token es válido o si la ruta no es una ruta protegida por este filtro,
        // continuar con la siguiente fase de la cadena de filtros.
        filterChain.doFilter(request, response);
    }
}
