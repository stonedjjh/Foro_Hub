package com.aluracurso.foro_hub_notifications_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importar
import org.springframework.context.annotation.Bean; // Importar
import org.springframework.security.web.SecurityFilterChain; // Importar
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importar

/**
 * Esta clase de configuración es usada en los tests de controladores
 * para habilitar el procesamiento de anotaciones como @PreAuthorize y
 * la cadena de filtros de seguridad.
 */
@Configuration
@EnableWebSecurity // Asegúrate de que esta anotación esté presente
public class TestSecurityConfiguration {

    // Necesitamos un SecurityFilterChain mínimo para que el ServiceTokenFilter se registre
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para tests
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Permitir todo por defecto en tests
        return http.build();
    }
}
