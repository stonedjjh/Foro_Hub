package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para los datos de bienvenida de un nuevo usuario.")
public class WelcomeUserData { // Asegúrate de que es una clase, no un record, si tienes este error

    @Schema(description = "Dirección de correo electrónico del usuario.", example = "nuevo.usuario@example.com")
    private String email;
    @Schema(description = "Nombre de usuario del nuevo usuario.", example = "JuanPerez")
    private String username;
    @Schema(description = "Contraseña generada automáticamente para el usuario.", example = "ContrasenaSegura123")
    private String password;

    // Constructor por defecto (necesario para algunos frameworks, como Spring al deserializar JSON)
    public WelcomeUserData() {
    }

    // Constructor con todos los argumentos (ESTE ES EL QUE FALTABA O ESTABA MAL)
    public WelcomeUserData(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters (pueden ser útiles, aunque para DTOs de entrada a veces no se necesitan)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}