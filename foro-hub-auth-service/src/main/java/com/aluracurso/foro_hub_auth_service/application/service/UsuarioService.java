package com.aluracurso.foro_hub_auth_service.application.service;

import com.aluracurso.foro_hub_auth_service.application.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.application.dto.DatosUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.application.dto.UsuarioDTO;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.PerfilRepository;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String generateRandomString(int length, String charSet) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charSet.length());
            sb.append(charSet.charAt(randomIndex));
        }
        return sb.toString();
    }

    public Usuario guardar(UsuarioDTO usuarioDTO){
        String clave = generateRandomString(8, this.charSet);
        System.out.println(clave);
        clave=passwordEncoder.encode(clave);
        var roles=usuarioDTO.idRol().stream()
                .map(r-> {
                    return perfilRepository.encontrarPorId(r)
                            .orElseThrow(() ->new NoSuchElementException(r.toString()));
                    }).toList();
        var usuario = new Usuario(usuarioDTO.nombre(),
                usuarioDTO.correoElectronico(),
                clave,
                roles);
       return usuarioRepository.guardar(usuario);
    }

    public void eliminar(Long id){
        usuarioRepository.eliminar(id);
        return;
    }

    public Usuario actualizar(Long id, DatosActualizarUsuarioDTO datosActualizarUsuarioDTO){
        var usuario = this.buscar(id);
        if(usuario!=null)
        {
            Optional.ofNullable(datosActualizarUsuarioDTO.nombre())
                    .ifPresent(usuario::setNombre);
            Optional.ofNullable(datosActualizarUsuarioDTO.correoElectronico())
                    .ifPresent(usuario::setCorreoElectronico);

            if (datosActualizarUsuarioDTO.permisos()!=null && !datosActualizarUsuarioDTO.permisos().isEmpty())
            {
                usuario.setPerfiles(datosActualizarUsuarioDTO.permisos()
                        .stream()
                        .map(
                        permiso-> perfilRepository.encontrarPorNombre(permiso.nombre()).orElseThrow(
                                ()-> new EntityNotFoundException()
                        ))
                        .toList()
                );
            }

        }else{
            throw new EntityNotFoundException();
        }

        return usuarioRepository.actualizar(usuario);
    }

    public Usuario buscar(Long id){
        return usuarioRepository.buscarPorId(id).
                orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario con el ID: " + id));
    }

    public List<Usuario> listar(){
        return usuarioRepository.listarTodos();
    }
}
