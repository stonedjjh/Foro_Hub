package com.aluracurso.foro_hub_auth_service.infraestructura.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Esta clase de configuración es usada en los tests de controladores
 * para habilitar el procesamiento de anotaciones como @PreAuthorize.
 */
@Configuration
@EnableMethodSecurity
public class TestSecurityConfiguration {

}
    