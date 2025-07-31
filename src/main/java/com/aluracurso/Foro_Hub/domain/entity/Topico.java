package com.aluracurso.Foro_Hub.domain.entity;

import com.aluracurso.Foro_Hub.aplication.dto.TopicoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private String status;
    private Long autor;

    @ManyToOne
    @JoinColumn(name="curso")
    private Curso curso;

    public Topico(TopicoDTO topicoDTO){
        this.titulo = topicoDTO.titulo();
        this.mensaje = topicoDTO.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.status = topicoDTO.status();

    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Topico topico = (Topico) o;
        if (id!=null)
            return Objects.equals(id, topico.id);
        return  Objects.equals(titulo, topico.titulo) && Objects.equals(mensaje, topico.mensaje) && Objects.equals(fechaCreacion, topico.fechaCreacion) && Objects.equals(status, topico.status) && Objects.equals(autor, topico.autor) && Objects.equals(curso, topico.curso);
    }

    @Override
    public int hashCode() {
        if (id!=null)
            return Objects.hash(id);
        return Objects.hash(titulo, mensaje, fechaCreacion, status, autor, curso);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate1 = fechaCreacion.format(formatter1);
        return String.format(
                """
                Id: %s
                Titulo: %s
                Mensaje: %s
                Fecha de creación: %s
                Estatus: %s
                Autor: %s
                Curso: %s
                        """,id,titulo,mensaje,formattedDate1, status,autor,curso
        );
    }
}
