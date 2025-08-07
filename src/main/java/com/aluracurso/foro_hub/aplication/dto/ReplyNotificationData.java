package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para los datos de notificación de una nueva respuesta en un tema.")
public class ReplyNotificationData {

    @Schema(description = "Correo electrónico del autor del tema original.", example = "autor.tema@example.com")
    private String recipientEmail;
    @Schema(description = "Título del tema del foro al que se respondió.", example = "Dudas sobre Microservicios")
    private String postTitle;
    @Schema(description = "Nombre del autor de la nueva respuesta.", example = "MariaLopez")
    private String replyAuthorName;
    @Schema(description = "Contenido de la respuesta.", example = "¡Excelente pregunta! Aquí mi aporte...")
    private String replyContent;

    // Constructor por defecto (necesario para la deserialización de JSON por Spring)
    public ReplyNotificationData() {
    }

    // Constructor con todos los argumentos (ESTE ES EL QUE FALTABA O ESTABA MAL)
    public ReplyNotificationData(String recipientEmail, String postTitle, String replyAuthorName, String replyContent) {
        this.recipientEmail = recipientEmail;
        this.postTitle = postTitle;
        this.replyAuthorName = replyAuthorName;
        this.replyContent = replyContent;
    }

    // Getters
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getReplyAuthorName() {
        return replyAuthorName;
    }

    public String getReplyContent() {
        return replyContent;
    }

    // Setters (pueden ser útiles, aunque para DTOs de entrada a veces no se necesitan)
    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setReplyAuthorName(String replyAuthorName) {
        this.replyAuthorName = replyAuthorName;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}