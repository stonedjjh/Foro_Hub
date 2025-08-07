package com.aluracurso.foro_hub.infrastructure.security;


import com.aluracurso.foro_hub.domain.usuario.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsFromEntity implements UserDetails {

    private Usuario usuario;

    public UserDetailsFromEntity(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.usuario.getPerfiles().stream()
                .map(p -> new SimpleGrantedAuthority(p.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.usuario.getClave();
    }

    @Override
    public String getUsername() {
        return this.usuario.getCorreoElectronico();
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
