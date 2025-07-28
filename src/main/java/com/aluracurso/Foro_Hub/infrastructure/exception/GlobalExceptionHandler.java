package com.aluracurso.Foro_Hub.infrastructure.exception;

import com.aluracurso.Foro_Hub.domain.topico.exception.TopicoDuplicadoException; // Importar la excepción de dominio
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Anotación para manejo global de excepciones en controladores REST
public class GlobalExceptionHandler {

    // Manejador específico para TopicoDuplicadoException
    @ExceptionHandler(TopicoDuplicadoException.class)
    public ResponseEntity<String> handleTopicoDuplicadoException(TopicoDuplicadoException ex) {
        // Devuelve un código de estado 409 Conflict con el mensaje de la excepción
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Manejador genérico para cualquier otra excepción no capturada específicamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Devuelve un código de estado 500 Internal Server Error con un mensaje genérico
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno en el servidor: " + ex.getMessage());
    }
}