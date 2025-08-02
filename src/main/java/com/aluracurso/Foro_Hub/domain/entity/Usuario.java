package com.aluracurso.Foro_Hub.domain.entity;

import com.aluracurso.Foro_Hub.aplication.dto.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Usuario implements UserDetails {
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
            name = "perfil_usuario", // Nombre de la tabla intermedia en la BD
            joinColumns = @JoinColumn(name = "id_usuario"), // Columna que referencia a esta entidad (Usuario)
            inverseJoinColumns = @JoinColumn(name = "id_perfil") // Columna que referencia a la otra entidad (Perfil)
    )
    private List<Perfil> perfiles;

    public Usuario(UsuarioDTO usuarioDTO){
        this.correoElectronico = usuarioDTO.correoElectronico();
        this.contraseña = usuarioDTO.clave();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  this.getPerfiles().stream()
                                .map(p->new SimpleGrantedAuthority(p.getNombre()))
                        .toList();
    }

    public String getPassword(){
        return this.contraseña;
    }

    @Override
    public String getUsername() {
        return this.correoElectronico;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
