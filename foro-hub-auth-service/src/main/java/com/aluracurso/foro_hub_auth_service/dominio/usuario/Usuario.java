// Archivo: Usuario.java
package com.aluracurso.foro_hub_auth_service.dominio.usuario;

import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Usuario implements UserDetails {

    private Long id;
    private String nombre;
    private String correoElectronico;
    private String clave;
    private List<Perfil> perfiles;

    public Usuario(Long id, String nombre, String correoElectronico, String clave) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.clave = clave;
        this.perfiles = new ArrayList<>();
    }

    public Usuario(String nombre, String correoElectronico, String clave, List<Perfil> rol) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.clave = clave;
        this.perfiles = new ArrayList<>();
        rol.forEach(r -> this.perfiles.add(r));
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfiles.stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_" + p.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return correoElectronico;
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

