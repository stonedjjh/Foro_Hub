package com.aluracurso.foro_hub_auth_service.infraestructura.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Manejador personalizado para errores de acceso denegado (403 Forbidden).
 * Devuelve un mensaje JSON en lugar de la página de error por defecto de Spring.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Establece el estado de la respuesta a 403 (Forbidden).
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Establece el tipo de contenido a JSON.
        response.setContentType("application/json");

        // Escribe el mensaje personalizado en el cuerpo de la respuesta.
        String jsonError = String.format("{\"error\": \"%s\"}",
                "No tienes permisos para realizar esta acción. Solo un administrador o el mismo usuario puede eliminar la cuenta.");
        response.getWriter().write(jsonError);
    }
}
