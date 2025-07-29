package com.aluracurso.Foro_Hub.domain.topico.entity;

import com.aluracurso.Foro_Hub.aplication.dto.UsuarioDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    public Usuario(UsuarioDTO usuarioDTO){
        this.correoElectronico = usuarioDTO.correoElectronico();
        this.contraseña = usuarioDTO.clave();
    }
}
