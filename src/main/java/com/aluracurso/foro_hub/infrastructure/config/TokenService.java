package com.aluracurso.foro_hub.infrastructure.config;



import com.aluracurso.foro_hub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;


    public String generarToken(Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(this.secret);
            return JWT.create()
                    .withIssuer("API foro.alura")
                    .withSubject(usuario.getCorreoElectronico())
                    .withClaim("id",usuario.getId())
                    .withClaim("permisos",
                            usuario.getPerfiles().stream()
                                    .map(p->"ROLE_" + p.getNombre())
                                    .toList()
                    )
                    .withExpiresAt(fechaExpiracion())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token jwt",exception);
        }
    }

    private Instant fechaExpiracion() {

        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-04:00"));
    }

    public String getSubject(String tokenJWT){
        try {
            var algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("API foro.alura")
                    // reusable verifier instance
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT Invalido o expirado!");
        }

    }

    public DecodedJWT verificarTokenYDecodificar(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API foro.alura")
                    .build()
                    .verify(tokenJWT);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido o expirado!", exception);
        }
    }
}