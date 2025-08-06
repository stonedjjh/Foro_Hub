package com.aluracurso.foro_hub_notifications_service.controller;

import com.aluracurso.foro_hub_notifications_service.config.TestSecurityConfiguration; // Importar la configuración de seguridad de prueba
import com.aluracurso.foro_hub_notifications_service.dto.ReplyNotificationData;
import com.aluracurso.foro_hub_notifications_service.dto.WelcomeUserData;
import com.aluracurso.foro_hub_notifications_service.infra.security.ServiceTokenFilter;
import com.aluracurso.foro_hub_notifications_service.service.ForoNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import({ServiceTokenFilter.class, TestSecurityConfiguration.class}) // Importar el filtro y la configuración de seguridad de prueba
@DisplayName("NotificationControllerTest - Pruebas de Integración")
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular peticiones HTTP

    @MockBean
    private ForoNotificationService notificationService; // Mock del servicio de notificaciones

    @Autowired
    private ServiceTokenFilter serviceTokenFilter; // Inyectar el filtro para configurar el secreto

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    private final String VALID_SECRET = "my-super-secret-test-key";
    private final String INVALID_SECRET = "wrong-secret";

    @BeforeEach
    void setUp() {
        // Inyectar el secreto compartido en el filtro de prueba
        ReflectionTestUtils.setField(serviceTokenFilter, "serviceToServiceSecret", VALID_SECRET);
    }

    // --- Pruebas para /notificaciones/bienvenida ---

    @Test
    @DisplayName("POST /notificaciones/bienvenida - Debe enviar correo con token válido")
    void sendWelcomeNotification_conTokenValido_debeRetornar200Ok() throws Exception {
        // 1. Preparación (Arrange)
        WelcomeUserData userData = new WelcomeUserData("test@example.com", "testuser", "password123");
        // Simular que el servicio no hace nada (no lanzará excepción)
        doNothing().when(notificationService).sendWelcomeNotification(anyString(), anyString(), anyString());

        // 2. Ejecución y Verificación (Act & Assert)
        mockMvc.perform(post("/notificaciones/bienvenida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Service-Token", VALID_SECRET) // Token válido
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isOk());

        // Verificar que el método del servicio fue llamado
        verify(notificationService, times(1)).sendWelcomeNotification(
                userData.getEmail(), userData.getUsername(), userData.getPassword());
    }

    @Test
    @DisplayName("POST /notificaciones/bienvenida - Debe denegar acceso sin token")
    void sendWelcomeNotification_sinToken_debeRetornar401Unauthorized() throws Exception {
        // 1. Preparación (Arrange)
        WelcomeUserData userData = new WelcomeUserData("test@example.com", "testuser", "password123");

        // 2. Ejecución y Verificación (Act & Assert)
        mockMvc.perform(post("/notificaciones/bienvenida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isUnauthorized());

        // Verificar que el método del servicio NO fue llamado
        verify(notificationService, never()).sendWelcomeNotification(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /notificaciones/bienvenida - Debe denegar acceso con token inválido")
    void sendWelcomeNotification_conTokenInvalido_debeRetornar401Unauthorized() throws Exception {
        // 1. Preparación (Arrange)
        WelcomeUserData userData = new WelcomeUserData("test@example.com", "testuser", "password123");

        // 2. Ejecución y Verificación (Act & Assert)
        mockMvc.perform(post("/notificaciones/bienvenida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Service-Token", INVALID_SECRET) // Token inválido
                        .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isUnauthorized());

        // Verificar que el método del servicio NO fue llamado
        verify(notificationService, never()).sendWelcomeNotification(anyString(), anyString(), anyString());
    }

    // --- Pruebas para /notificaciones/respuesta ---

    @Test
    @DisplayName("POST /notificaciones/respuesta - Debe enviar notificación con token válido")
    void sendReplyNotification_conTokenValido_debeRetornar200Ok() throws Exception {
        // 1. Preparación (Arrange)
        ReplyNotificationData replyData = new ReplyNotificationData(
                "autor@example.com", "Mi Tema", "Respondedor", "Contenido de la respuesta");
        doNothing().when(notificationService).sendReplyNotification(anyString(), anyString(), anyString(), anyString());

        // 2. Ejecución y Verificación (Act & Assert)
        mockMvc.perform(post("/notificaciones/respuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Service-Token", VALID_SECRET) // Token válido
                        .content(objectMapper.writeValueAsString(replyData)))
                .andExpect(status().isOk());

        // Verificar que el método del servicio fue llamado
        verify(notificationService, times(1)).sendReplyNotification(
                replyData.getRecipientEmail(), replyData.getPostTitle(),
                replyData.getReplyAuthorName(), replyData.getReplyContent());
    }

    @Test
    @DisplayName("POST /notificaciones/respuesta - Debe denegar acceso sin token")
    void sendReplyNotification_sinToken_debeRetornar401Unauthorized() throws Exception {
        // 1. Preparación (Arrange)
        ReplyNotificationData replyData = new ReplyNotificationData(
                "autor@example.com", "Mi Tema", "Respondedor", "Contenido de la respuesta");

        // 2. Ejecución y Verificación (Act & Assert)
        mockMvc.perform(post("/notificaciones/respuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replyData)))
                .andExpect(status().isUnauthorized());

        // Verificar que el método del servicio NO fue llamado
        verify(notificationService, never()).sendReplyNotification(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /notificaciones/respuesta - Debe denegar acceso con token inválido")
    void sendReplyNotification_conTokenInvalido_debeRetornar401Unauthorized() throws Exception {
        // 1. Preparación (Arrange)
        ReplyNotificationData replyData = new ReplyNotificationData(
                "autor@example.com", "Mi Tema", "Respondedor", "Contenido de la respuesta");

        // 2. Ejecución y Verificación (Act & Assert)
        mockMvc.perform(post("/notificaciones/respuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Service-Token", INVALID_SECRET) // Token inválido
                        .content(objectMapper.writeValueAsString(replyData)))
                .andExpect(status().isUnauthorized());

        // Verificar que el método del servicio NO fue llamado
        verify(notificationService, never()).sendReplyNotification(anyString(), anyString(), anyString(), anyString());
    }
}

