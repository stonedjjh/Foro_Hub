package com.aluracurso.foro_hub.infrastructure.persistence;

import com.aluracurso.foro_hub.aplication.dto.TopicoDTO;
import com.aluracurso.foro_hub.domain.curso.Curso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "topicos")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor")
    private Usuario autor;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Respuesta> respuestas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="curso")
    private Curso curso;

    public Topico(TopicoDTO topicoDTO){
        this.titulo = topicoDTO.titulo();
        this.mensaje = topicoDTO.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.status = topicoDTO.status();
    }

    public Topico(@NotNull Long aLong) {
        this.id = aLong;
    }

    public Topico(LocalDateTime fecha) {
        this.fechaCreacion = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topico topico = (Topico) o;
        return Objects.equals(id, topico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
                """, id, titulo, mensaje, formattedDate1, status, autor.getNombre(), curso.getNombre()
        );
    }
}
