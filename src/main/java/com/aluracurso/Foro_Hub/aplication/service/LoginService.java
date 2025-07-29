package com.aluracurso.Foro_Hub.aplication.service;

import com.aluracurso.Foro_Hub.aplication.dto.UsuarioDTO;
import com.aluracurso.Foro_Hub.domain.topico.entity.Usuario;
import com.aluracurso.Foro_Hub.domain.topico.exception.TopicoNoEncontradoException;
import com.aluracurso.Foro_Hub.domain.topico.exception.UsuarioNoEncontradoException;
import com.aluracurso.Foro_Hub.domain.topico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private PasswordEncoder passwordEncoder; // BCryptPasswordEncoder es una implementación de PasswordEncoder
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean verificarContrasena(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String verificarUsuario(UsuarioDTO usuarioDTO) throws UsuarioNoEncontradoException
    {

        Usuario usuario = usuarioRepository.findByCorreoElectronico(usuarioDTO.correoElectronico())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Credenciales no coinciden"));
        System.out.println("Contraseña del DTO (clave): " + usuarioDTO.clave()); // <-- Nueva línea
        System.out.println("Contraseña recuperada de la BD: " + usuario.getContraseña());
        System.out.println("Longitud de la contraseña recuperada: " + usuario.getContraseña().length()); // <-- Nueva línea

        if(this.verificarContrasena(usuarioDTO.clave(), usuario.getContraseña())){
            return "usuario valido";
        }

        throw new UsuarioNoEncontradoException("Credenciales no coinciden");

    }


}
