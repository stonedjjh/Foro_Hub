package com.aluracurso.foro_hub_auth_service.aplicacion.service;

import com.aluracurso.foro_hub_auth_service.aplicacion.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.aplicacion.dto.UsuarioDTO;
import com.aluracurso.foro_hub_auth_service.dominio.exceptions.CorreoElectronicoDuplicadoException;
import com.aluracurso.foro_hub_auth_service.dominio.exceptions.PerfilNoEncontradoException;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.PerfilRepository;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page; // Importado para la paginación
import org.springframework.data.domain.Pageable;


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

    public Usuario guardar(UsuarioDTO usuarioDTO) {
        var existe = this.buscarPorCorreoElectronico(usuarioDTO.correoElectronico());
        if(existe.isPresent()){
            throw new CorreoElectronicoDuplicadoException("El correo electrónico " + usuarioDTO.correoElectronico() + " ya esta registrado");
        }

            String clave = generateRandomString(8, this.charSet);
            System.out.println(clave);
            clave=passwordEncoder.encode(clave);
            var roles=usuarioDTO.idRol().stream()
                    .map(r-> {
                        return perfilRepository.encontrarPorId(r)
                                .orElseThrow(() ->new PerfilNoEncontradoException(r.toString()));
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
                        permiso-> perfilRepository.encontrarPorId(permiso.id()).orElseThrow(
                                ()-> new PerfilNoEncontradoException(permiso.nombre())
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

    public Optional<Usuario> buscarPorCorreoElectronico(String correoElectronico){
            return usuarioRepository.buscarPorCorreoElectronico(correoElectronico);
    }

    // Método 'listar' actualizado para usar paginación
    public Page<Usuario> listar(Pageable pageable){
        // Usamos el método findAll de Spring Data JPA, que ya soporta paginación
        // Tu JpaUsuarioRepository debe extender JpaRepository<Usuario, Long>
        return usuarioRepository.listarTodos(pageable);
    }
}
