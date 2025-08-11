package com.aluracurso.foro_hub.domain.topico;

import com.aluracurso.foro_hub.aplication.dto.TopicoDTO;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

// Esta es la clase de dominio, no tiene anotaciones JPA.
// Se enfoca en representar el objeto de negocio.
public class Topico {
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private String status;
    private Long autor; // Referencia simple al ID del autor
    private Long curso; // Referencia simple al ID del curso

    public Topico(Long id, String titulo, String mensaje, LocalDateTime fechaCreacion, String status, Long autorId, Long cursoId) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion;
        this.status = status;
        this.autor = autorId;
        this.curso = cursoId;
    }

    public Topico() {
    }

    public Topico(TopicoDTO topicoDTO){
        this.titulo = topicoDTO.titulo();
        this.mensaje = topicoDTO.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.status = topicoDTO.status();
        this.curso = topicoDTO.curso();
    }

    public Topico(@NotNull Long aLong) {
        this.id = aLong;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAutor() {
        return autor;
    }

    public void setAutor(Long autorId) {
        this.autor = autorId;
    }

    public Long getCurso() {
        return curso;
    }

    public void setCurso(Long cursoId) {
        this.curso = cursoId;
    }
}


