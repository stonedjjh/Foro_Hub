package com.aluracurso.foro_hub_notifications_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Foro Alura - Servicio de Notificaciones")
                        .description("Este es el microservicio de notificaciones de Foro Alura. Proporciona endpoints " +
                                "para el envío de correos electrónicos de bienvenida y notificaciones de respuestas en " +
                                "el foro. Diseñado para comunicación INTERNA entre microservicios.")
                        .contact(new Contact()
                                .name("Daniel Jiménez")
                                .email("danjim82@gmail.com"))
                        .license(new License()
                                .name("Fines Educativos - Curso Alura Latam")
                                .url("https://github.com/stonedjjh/Foro_Hub")));
    }
}