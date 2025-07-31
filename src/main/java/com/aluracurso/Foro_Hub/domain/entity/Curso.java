package com.aluracurso.Foro_Hub.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Curso {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String categoria;

    @OneToMany(targetEntity = Topico.class,mappedBy = "curso" )
    private List<Topico> topico =new ArrayList<>();

}
