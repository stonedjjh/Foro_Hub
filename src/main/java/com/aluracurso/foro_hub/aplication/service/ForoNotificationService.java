package com.aluracurso.foro_hub.aplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ForoNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // Esta propiedad se leerá del application.properties
    private final String fromEmail = "infoaluraforo@gmail.com";

    /**
     * Envía una notificación por correo electrónico al autor del tema cuando alguien responde.
     *
     * @param recipientEmail El correo del autor del tema original.
     * @param postTitle El título del tema que ha sido respondido.
     * @param replyAuthorName El nombre del usuario que ha respondido.
     * @param replyContent El contenido de la respuesta.
     */
    public void sendReplyNotification(String recipientEmail, String postTitle, String replyAuthorName, String replyContent) {
        try {
            // Crea un nuevo mensaje de correo
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Configura el remitente, el destinatario y el asunto
            helper.setFrom(fromEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("¡Nueva respuesta en tu tema: '" + postTitle + "'!");

            // Usa un contexto de Thymeleaf para pasar las variables a la plantilla
            Context context = new Context();
            context.setVariable("postTitle", postTitle);
            context.setVariable("replyAuthorName", replyAuthorName);
            context.setVariable("replyContent", replyContent);

            // Procesa la plantilla de HTML con los datos del contexto
            String htmlContent = templateEngine.process("reply-notification", context);

            // Establece el contenido del correo como HTML
            helper.setText(htmlContent, true);

            // Envía el correo
            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de notificación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envía un correo de bienvenida a un nuevo usuario con su nombre de usuario y contraseña generada.
     *
     * @param recipientEmail El correo del nuevo usuario.
     * @param username El nombre de usuario del nuevo usuario.
     * @param password La contraseña generada automáticamente para el nuevo usuario.
     */
    public void sendWelcomeNotification(String recipientEmail, String username, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("¡Bienvenido a Foro Alura(no oficial)!");

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("password", password);

            String htmlContent = templateEngine.process("welcome-notification", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
