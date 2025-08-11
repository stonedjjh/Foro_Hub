package com.aluracurso.foro_hub.domain.respuesta;


import com.aluracurso.foro_hub.domain.topico.Topico;
import com.aluracurso.foro_hub.domain.usuario.Usuario;


import java.time.LocalDateTime;


public class Respuesta {
    private Long id;
    private Topico topico;   // Referencia al objeto Tópico completo
    private Usuario autor;   // Referencia al objeto Usuario completo
    private String mensaje;
    private Boolean solucion;
    private LocalDateTime fechaCreacion;

    public Respuesta(Long id, Topico topico, Usuario autor, String mensaje) {
        this.id = id;
        this.topico = topico;
        this.autor = autor;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now();

    }

    public Respuesta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topico getTopico() {
        return topico;
    }

    public void setTopico(Topico topico) {
        this.topico = topico;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getSolucion() {
        return solucion;
    }

    public void setSolucion(Boolean solucion) {
        this.solucion = solucion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Respuesta{" +
                "id=" + id +
                ", topico=" + topico +
                ", autor=" + autor +
                ", mensaje='" + mensaje + '\'' +
                ", solucion=" + solucion +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
