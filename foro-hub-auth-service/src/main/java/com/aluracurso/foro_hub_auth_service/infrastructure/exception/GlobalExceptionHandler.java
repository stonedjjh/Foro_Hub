package com.aluracurso.foro_hub_auth_service.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice // Anotación para manejo global de excepciones en controladores REST
public class GlobalExceptionHandler {



    // Manejador genérico para cualquier otra excepción no capturada específicamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Devuelve un código de estado 500 Internal Server Error con un mensaje genérico
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno en el servidor: " + ex.getMessage());
    }

    // ¡NUEVO MANEJADOR DE EXCEPCIONES!
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("El parámetro '%s' con valor '%s' no es del tipo esperado. Se esperaba un tipo numérico (Long).",
                ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleUsuarioNoEncontradoException(BadCredentialsException ex) {
        // Devuelve un código de estado 401 Not Authorizado con el mensaje de la excepción
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }



}