package com.aluracurso.foro_hub.infrastructure.config;

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
                                                .title("Foro Alura API")
                                                .description("Foro_Hub es una API REST segura en Spring Boot para gestionar tópicos de" +
                                                        " discusión. Ofrece un completo CRUD, autenticación JWT para protección de " +
                                                        "endpoints y documentación interactiva generada con OpenAPI (Swagger UI)")
                                                .contact(new Contact()
                                                        .name("Daniel Jiménez")
                                                        .email("danjim82@gmail.com"))
                                                .license(new License()
                                                        .name("Fines Educativos - Curso Alura Latam")
                                                        .url("https://github.com/stonedjjh/Foro_Hub")));
    }
}
