package com.aluracurso.foro_hub_notifications_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para el envío de un correo de bienvenida a un nuevo usuario.")
public class WelcomeUserData {
    @Schema(description = "Dirección de correo electrónico del destinatario.", example = "nuevo.usuario@example.com")
    private String email;
    @Schema(description = "Nombre de usuario del destinatario.", example = "JuanPerez")
    private String username;
    @Schema(description = "Contraseña temporal o generada para el destinatario.", example = "ContrasenaSegura123")
    private String password;

    // Getters y setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}