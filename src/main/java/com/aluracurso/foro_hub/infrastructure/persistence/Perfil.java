package com.aluracurso.foro_hub.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perfiles")
@Entity
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_usuario", // Nombre de la tabla intermedia en la BD
            joinColumns = @JoinColumn(name = "id_perfil"), // Columna que referencia a esta entidad (Perfil)
            inverseJoinColumns = @JoinColumn(name = "id_usuario") // Columna que referencia a la otra entidad (Usuario)
    )
    private List<Usuario> usuarios;

}