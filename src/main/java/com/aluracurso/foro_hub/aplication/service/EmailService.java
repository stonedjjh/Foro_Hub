package com.aluracurso.foro_hub.aplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // Método para enviar correos electrónicos con un template HTML
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> templateVariables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            // Define el contexto para el template de Thymeleaf
            Context context = new Context();
            context.setVariables(templateVariables);

            // Procesa el template HTML con las variables
            String htmlContent = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que es contenido HTML

            mailSender.send(message);

            System.out.println("Correo enviado a " + to);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo a " + to + ": " + e.getMessage());
            // Aquí podrías agregar un log o manejar el error de otra manera
        }
    }
}