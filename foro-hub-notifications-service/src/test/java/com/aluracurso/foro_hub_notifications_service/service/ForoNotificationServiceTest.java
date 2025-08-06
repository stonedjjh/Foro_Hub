package com.aluracurso.foro_hub_notifications_service.service;


import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.test.util.ReflectionTestUtils; // Para inyectar @Value

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ForoNotificationServiceTest - Pruebas Unitarias del Servicio")
class ForoNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender; // Mock del enviador de correos
    @Mock
    private SpringTemplateEngine templateEngine; // Mock del motor de plantillas
    @Mock
    private MimeMessage mimeMessage; // Mock del mensaje MIME

    @InjectMocks
    private ForoNotificationService foroNotificationService; // Clase a probar

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializar los mocks

        // Configurar el mock de mailSender para devolver un MimeMessage mockeado
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Inyectar el valor de 'fromEmail' ya que es una propiedad @Value
        ReflectionTestUtils.setField(foroNotificationService, "fromEmail", "test@foro.com");
    }

    @Test
    @DisplayName("sendWelcomeNotification - Debe enviar un correo de bienvenida correctamente")
    void sendWelcomeNotification_debeEnviarCorreo() {
        // 1. Preparación (Arrange)
        String to = "nuevo.usuario@example.com";
        String username = "NuevoUsuario";
        String password = "claveGenerada";
        String htmlContentSimulated = "<html><body>Bienvenido</body></html>";

        // Simular que el templateEngine procesa la plantilla
        when(templateEngine.process(eq("welcome-notification"), any(Context.class)))
                .thenReturn(htmlContentSimulated);

        // 2. Ejecución (Act)
        foroNotificationService.sendWelcomeNotification(to, username, password);

        // 3. Verificación (Assert)

        // Verificar que se creó un MimeMessage
        verify(mailSender, times(1)).createMimeMessage();

        // Capturar el MimeMessage para verificar sus propiedades
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        MimeMessage sentMessage = messageCaptor.getValue();

        // Verificar que se procesó la plantilla correcta con el contexto adecuado
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine, times(1)).process(eq("welcome-notification"), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();
        assertEquals(username, capturedContext.getVariable("username"));
        assertEquals(password, capturedContext.getVariable("password"));

        // No podemos verificar directamente el contenido del MimeMessageHelper
        // pero sí que el mailSender fue invocado con el mensaje.
    }

    @Test
    @DisplayName("sendReplyNotification - Debe enviar un correo de notificación de respuesta correctamente")
    void sendReplyNotification_debeEnviarCorreo() {
        // 1. Preparación (Arrange)
        String recipientEmail = "autor.tema@example.com";
        String postTitle = "Mi Tema Importante";
        String replyAuthorName = "RespondedorGenial";
        String replyContent = "¡Gran respuesta!";
        String htmlContentSimulated = "<html><body>Nueva respuesta</body></html>";

        // Simular que el templateEngine procesa la plantilla
        when(templateEngine.process(eq("reply-notification"), any(Context.class)))
                .thenReturn(htmlContentSimulated);

        // 2. Ejecución (Act)
        foroNotificationService.sendReplyNotification(recipientEmail, postTitle, replyAuthorName, replyContent);

        // 3. Verificación (Assert)

        // Verificar que se creó un MimeMessage
        verify(mailSender, times(1)).createMimeMessage();

        // Capturar el MimeMessage para verificar sus propiedades
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        MimeMessage sentMessage = messageCaptor.getValue();

        // Verificar que se procesó la plantilla correcta con el contexto adecuado
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine, times(1)).process(eq("reply-notification"), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();
        assertEquals(postTitle, capturedContext.getVariable("postTitle"));
        assertEquals(replyAuthorName, capturedContext.getVariable("replyAuthorName"));
        assertEquals(replyContent, capturedContext.getVariable("replyContent"));
    }
}

