package com.aluracurso.foro_hub.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "usuarios")
@Entity
public class Usuario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    @NotBlank
    String nombre;
    @NotBlank
    @Email
    String correoElectronico;
    @NotBlank
    String contraseña;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_usuario",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil")
    )
    private List<Perfil> perfiles;

    public Usuario(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", perfiles=" + perfiles +
                '}';
    }
}
