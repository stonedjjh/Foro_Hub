package com.aluracurso.foro_hub_auth_service.dominio.perfil;

public class Perfil {
    private Long id;
    private String nombre;

    public Perfil(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}