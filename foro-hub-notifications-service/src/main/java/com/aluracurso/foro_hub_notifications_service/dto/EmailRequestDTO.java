package com.aluracurso.foro_hub_notifications_service.dto;

public class EmailRequestDTO {
    private String to;
    private String subject;
    private String name;
    private String message;

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}