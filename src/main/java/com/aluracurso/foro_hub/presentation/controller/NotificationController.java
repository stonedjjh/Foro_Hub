package com.aluracurso.foro_hub.presentation.controller;


import com.aluracurso.foro_hub.aplication.dto.ReplyNotificationData;
import com.aluracurso.foro_hub.aplication.dto.WelcomeUserData;
import com.aluracurso.foro_hub.aplication.service.ForoNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notificaciones") // Ruta base para todas las notificaciones
@Tag(name = "Notificaciones", description = "Endpoints para la gestión de envío de correos electrónicos y otras notificaciones.")
public class NotificationController { // Nombre más genérico para el microservicio

    @Autowired
    private ForoNotificationService notificationService;

    /**
     * Endpoint para enviar un correo de bienvenida a un nuevo usuario.
     * Este endpoint es para uso INTERNO entre microservicios.
     *
     * @param userData Un objeto que contiene el email, nombre de usuario y contraseña temporal.
     * @return Una respuesta HTTP 200 si el correo se envía correctamente.
     */
    @Operation(
            summary = "Envía un correo de bienvenida a un nuevo usuario.",
            description = "Este endpoint es para uso INTERNO entre microservicios. " +
                    "Recibe los datos de un nuevo usuario para enviarle un correo de bienvenida con sus credenciales.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Correo de bienvenida enviado con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error al enviar el correo."),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
            }
    )
    @PostMapping("/bienvenida")
    public ResponseEntity<Void> sendWelcomeNotification(@RequestBody WelcomeUserData userData) {
        notificationService.sendWelcomeNotification(
                userData.getEmail(),
                userData.getUsername(),
                userData.getPassword()
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para enviar una notificación al autor de un tema cuando alguien responde.
     * Este endpoint es para uso INTERNO entre microservicios.
     *
     * @param replyData Un objeto con los detalles de la respuesta y el tema.
     * @return Una respuesta HTTP 200 si el correo se envía correctamente.
     */
    @Operation(
            summary = "Envía una notificación de nueva respuesta en un tema del foro.",
            description = "Este endpoint es para uso INTERNO entre microservicios. " +
                    "Notifica al autor del tema original que ha recibido una nueva respuesta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notificación de respuesta enviada con éxito."),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o error al enviar el correo."),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
            }
    )
    @PostMapping("/respuesta")
    public ResponseEntity<Void> sendReplyNotification(@RequestBody ReplyNotificationData replyData) {
        notificationService.sendReplyNotification(
                replyData.getRecipientEmail(),
                replyData.getPostTitle(),
                replyData.getReplyAuthorName(),
                replyData.getReplyContent()
        );
        return ResponseEntity.ok().build();
    }
}