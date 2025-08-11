package com.aluracurso.foro_hub.domain.curso;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Importamos la clase Topico correcta
import com.aluracurso.foro_hub.infrastructure.persistence.Topico;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String categoria;

    // Se corrige la referencia para apuntar a la clase Topico correcta.
    // También se puede omitir targetEntity, ya que se infiere de la lista.
    @OneToMany(mappedBy = "curso")
    private List<Topico> topicos = new ArrayList<>();

    public Curso(Long curso) {
    this.id = curso;
    }
}
