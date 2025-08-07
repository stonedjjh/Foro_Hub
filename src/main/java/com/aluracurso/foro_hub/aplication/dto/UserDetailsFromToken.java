package com.aluracurso.foro_hub.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Schema(description = "DTO que implementa UserDetails, creado a partir de los datos de un token JWT.")
public record UserDetailsFromToken(
        @Schema(description = "ID del usuario extraído del token")
        Long id,
        @Schema(description = "Nombre de usuario (correo electrónico) extraído del token")
        String username,
        @Schema(description = "Lista de roles (perfiles) extraídos del token")
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null; // El token ya está verificado, no necesitamos la contraseña
    }

    @Override
    public String getUsername() {
        return this.username;
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
