package com.aluracurso.foro_hub_notifications_service.config; // Ajusta el paquete según tu estructura

import com.aluracurso.foro_hub_notifications_service.infra.security.ServiceTokenFilter; // Importar el nuevo filtro
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private ServiceTokenFilter serviceTokenFilter; // Inyectar el filtro de token de servicio

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST sin estado
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Usar sesiones sin estado
                .authorizeHttpRequests(authorize -> authorize
                        // Permitir acceso a la documentación de Swagger UI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        // Proteger los endpoints de notificaciones: requieren autenticación (del filtro de servicio)
                        .requestMatchers(HttpMethod.POST, "/notificaciones/**").authenticated()
                        // Cualquier otra petición no especificada se permite públicamente por defecto
                        // Esto es útil si no tienes otras APIs que necesiten protección JWT de usuario final
                        .anyRequest().permitAll()
                )
                // Añadir el ServiceTokenFilter ANTES de los filtros estándar de autenticación de Spring
                .addFilterBefore(serviceTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
