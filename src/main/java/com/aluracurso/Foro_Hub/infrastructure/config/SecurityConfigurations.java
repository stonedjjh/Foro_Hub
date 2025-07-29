package com.aluracurso.Foro_Hub.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//Esto para indicarle que se modificara la configuracion del paquete security
@EnableWebSecurity
public class SecurityConfigurations {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST sin estado
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login").permitAll() // Permitir acceso público a /login
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                // Aquí irían las configuraciones para JWT o el tipo de autenticación
                // .addFilterBefore(tuFiltroDeAutenticacionJWT, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}