package com.aluracurso.foro_hub_auth_service.infraestructura.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.core.AuthenticationException;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("El parámetro '%s' con valor '%s' no es del tipo esperado. Se esperaba un tipo numérico (Long).",
                ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DatosErrorValidacion>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<DatosErrorValidacion> errores = ex.getFieldErrors().stream()
                .map(DatosErrorValidacion::new)
                .toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DatosError> handleAuthenticationError(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DatosError("Credenciales erróneas"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<DatosError> NoSuchElementExceptionError(NoSuchElementException ex) {
        return ResponseEntity.badRequest().body(new DatosError("Perfil " +ex +" no existe"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DatosError> EntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }



    public record DatosError(String mensaje) {
    }

    public record DatosErrorValidacion(
            String campo,
            String error
    ) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}