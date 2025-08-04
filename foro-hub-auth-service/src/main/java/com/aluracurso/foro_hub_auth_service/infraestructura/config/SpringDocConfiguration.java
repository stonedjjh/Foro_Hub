package com.aluracurso.foro_hub_auth_service.infraestructura.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer").bearerFormat("JWT")))
                                        .info(new Info()
                                                .title("Foro Alura - Servicio de Autenticación")
                                                .description("Este es el microservicio de autenticación de Foro Alura. " +
                                                        "Proporciona endpoints para el registro y login de usuarios, " +
                                                        "gestión de perfiles y la emisión de tokens JWT, desacoplando " +
                                                        "la lógica de seguridad del servicio principal del foro.")
                                                .contact(new Contact()
                                                        .name("Daniel Jiménez")
                                                        .email("danjim82@gmail.com"))
                                                .license(new License()
                                                        .name("Fines Educativos - Curso Alura Latam")
                                                        .url("https://github.com/stonedjjh/Foro_Hub")));
    }
}
