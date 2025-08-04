package com.aluracurso.foro_hub_auth_service.presentation.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Anotación de prueba para crear un principal de usuario con un ID personalizado.
 * Esta anotación es necesaria para las pruebas que utilizan expresiones @PreAuthorize
 * que acceden al campo 'id' del principal.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String username() default "user@example.com";
    String[] roles() default {"USUARIO"};
}

