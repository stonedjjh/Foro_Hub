package com.aluracurso.foro_hub_notifications_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para el envío de una notificación de respuesta a un tema del foro.")
public class ReplyNotificationData {
    @Schema(description = "Correo electrónico del autor del tema original.", example = "autor@example.com")
    private String recipientEmail;
    @Schema(description = "Título del tema que ha recibido la respuesta.", example = "Dudas sobre Spring Security")
    private String postTitle;
    @Schema(description = "Nombre del autor de la respuesta.", example = "MariaLopez")
    private String replyAuthorName;
    @Schema(description = "Contenido de la respuesta.", example = "Hola, sobre tu duda, puedes revisar la documentación oficial de Spring...")
    private String replyContent;

    // Getters y setters
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getPostTitle() { return postTitle; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

    public String getReplyAuthorName() { return replyAuthorName; }
    public void setReplyAuthorName(String replyAuthorName) { this.replyAuthorName = replyAuthorName; }

    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }
}
