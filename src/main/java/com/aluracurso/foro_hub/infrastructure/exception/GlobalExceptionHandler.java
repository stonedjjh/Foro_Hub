package com.aluracurso.foro_hub.infrastructure.exception;

import com.aluracurso.foro_hub.domain.curso.exception.CursoNoEncontradoException;
import com.aluracurso.foro_hub.domain.perfil.exception.PerfilNoEncontradoException;
import com.aluracurso.foro_hub.domain.respuesta.exception.SolucionYaMarcadaException;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoDuplicadoException;
import com.aluracurso.foro_hub.domain.topico.exception.TopicoNoEncontradoException;
import com.aluracurso.foro_hub.domain.usuario.exception.CorreoElectronicoDuplicadoException;
import com.aluracurso.foro_hub.infrastructure.persistence.exception.RespuestaNoEncontradaException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase manejadora de excepciones global para la aplicación.
 * Utiliza la anotación {@code @RestControllerAdvice} para centralizar el manejo de excepciones
 * de todos los controladores REST. Esto permite una gestión consistente y estandarizada
 * de los errores HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción {@code TopicoDuplicadoException}.
     * Se activa cuando se intenta crear un tópico con un título o mensaje que ya existe.
     *
     * @param ex La excepción {@code TopicoDuplicadoException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 409 (Conflict) y el mensaje de la excepción.
     */
    @ExceptionHandler(TopicoDuplicadoException.class)
    public ResponseEntity<String> handleTopicoDuplicadoException(TopicoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Maneja excepciones genéricas no capturadas por otros manejadores.
     * Se usa como un "fallback" para errores inesperados del servidor.
     *
     * @param ex La excepción genérica que fue lanzada.
     * @return Una respuesta HTTP con el estado 500 (Internal Server Error) y un mensaje descriptivo.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno en el servidor: " + ex.getMessage());
    }

    /**
     * Maneja la excepción {@code MethodArgumentTypeMismatchException}.
     * Se activa cuando un parámetro de una URL (como un ID) no coincide con el tipo esperado (ej: String en lugar de Long).
     *
     * @param ex La excepción {@code MethodArgumentTypeMismatchException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 400 (Bad Request) y un mensaje de error detallado.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("El parámetro '%s' con valor '%s' no es del tipo esperado. Se esperaba un tipo numérico (Long).",
                ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja la excepción {@code TopicoNoEncontradoException}.
     * Se activa cuando se intenta acceder o modificar un tópico que no existe en la base de datos.
     *
     * @param ex La excepción {@code TopicoNoEncontradoException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 404 (Not Found) y el mensaje de la excepción.
     */
    @ExceptionHandler(TopicoNoEncontradoException.class)
    public ResponseEntity<String> handleTopicoNoEncontradoException(TopicoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Maneja la excepción {@code BadCredentialsException}.
     * Se activa cuando un usuario intenta iniciar sesión con credenciales incorrectas.
     *
     * @param ex La excepción {@code BadCredentialsException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 401 (Unauthorized) y el mensaje de la excepción.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Maneja la excepción {@code CursoNoEncontradoException}.
     * Se activa cuando se intenta asociar un tópico a un curso que no existe.
     *
     * @param ex La excepción {@code CursoNoEncontradoException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 404 (Not Found) y el mensaje de la excepción.
     */
    @ExceptionHandler(CursoNoEncontradoException.class)
    public ResponseEntity<String> handleCursoNoEncontradoException(CursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Maneja la excepción {@code CorreoElectronicoDuplicadoException}.
     * Se activa cuando se intenta registrar un usuario con un correo electrónico que ya existe.
     *
     * @param ex La excepción que fue lanzada.
     * @return Una respuesta HTTP con el estado 409 (Conflict) y el mensaje de la excepción.
     */
    @ExceptionHandler(CorreoElectronicoDuplicadoException.class)
    public ResponseEntity<String> manejarCorreoElectronicoDuplicado(CorreoElectronicoDuplicadoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Maneja la excepción {@code PerfilNoEncontradoException}.
     * Se activa cuando un perfil no es encontrado en la base de datos.
     *
     * @param ex La excepción {@code PerfilNoEncontradoException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 400 (Bad Request) y un DTO de error.
     */
    @ExceptionHandler(PerfilNoEncontradoException.class)
    public ResponseEntity<DatosError> handlePerfilNoEncontradoException(PerfilNoEncontradoException ex) {
        return ResponseEntity.badRequest().body(new DatosError("Perfil no existe"));
    }

    /**
     * Maneja la excepción {@code MethodArgumentNotValidException}.
     * Se activa cuando la validación de un DTO de entrada falla. Devuelve todos los errores de validación.
     *
     * @param ex La excepción {@code MethodArgumentNotValidException} que contiene los errores de validación.
     * @return Una respuesta HTTP con el estado 400 (Bad Request) y una lista de DTOs con los errores.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DatosErrorValidacion>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<DatosErrorValidacion> errores = ex.getFieldErrors().stream()
                .map(DatosErrorValidacion::new)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errores);
    }

    /**
     * Maneja la excepción {@code AuthenticationException}.
     * Captura errores de autenticación genéricos, como un token JWT inválido.
     *
     * @param ex La excepción {@code AuthenticationException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 401 (Unauthorized) y un DTO de error.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DatosError> handleAuthenticationError(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DatosError("Credenciales erróneas"));
    }

    /**
     * Maneja las excepciones de autorización de Spring Security y devuelve
     * un 403 Forbidden.
     * @param e La excepción de tipo AuthorizationDeniedException o AccessDeniedException.
     * @return ResponseEntity con un mensaje de error y el estado 403.
     */
    @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<DatosError> handleAuthorizationDeniedException(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new DatosError("Acceso denegado. No tienes permisos para realizar esta acción."));
    }

    /**
     * Maneja la excepción {@code EntityNotFoundException}.
     * Esta excepción es lanzada por JPA/Hibernate cuando una entidad no se encuentra.
     *
     * @param ex La excepción {@code EntityNotFoundException} que fue lanzada.
     * @return Una respuesta HTTP con el estado 404 (Not Found).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> EntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    /**
     * Maneja la excepción ExcepcionSolucionYaMarcada.
     * La anotación @ExceptionHandler especifica qué excepción este método tratará.
     *
     * @param ex El objeto de excepción capturado.
     * @return Un ResponseEntity con un mensaje de error y el estado HTTP 400.
     */
    @ExceptionHandler(SolucionYaMarcadaException.class)
    public ResponseEntity<String> manejarExcepcionSolucionYaMarcada(SolucionYaMarcadaException ex) {
        // Retorna un ResponseEntity con el mensaje de la excepción y el estado HTTP BAD_REQUEST (400).
        // Un estado 400 es apropiado porque la petición del cliente es inválida
        // con base en el estado actual del recurso.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Nuevo método para manejar la excepción RespuestaNoEncontradaException.
     *
     * @param ex El objeto de excepción capturado.
     * @return Un ResponseEntity con un mensaje de error y el estado HTTP 404 (Not Found).
     */
    @ExceptionHandler(RespuestaNoEncontradaException.class)
    public ResponseEntity<String> manejarRespuestaNoEncontrada(RespuestaNoEncontradaException ex) {
        // Retorna un ResponseEntity con el mensaje de la excepción y el estado HTTP NOT_FOUND (404).
        // Este estado indica que el recurso solicitado no pudo ser encontrado.
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Un registro (record) para representar un mensaje de error genérico.
     * Contiene un único campo para el mensaje.
     */
    public record DatosError(String mensaje) {
    }

    /**
     * Un registro (record) para representar un error de validación de campos.
     * Contiene el nombre del campo y el mensaje de error asociado.
     */
    public record DatosErrorValidacion(
            String campo,
            String error
    ) {
        /**
         * Constructor que acepta un objeto {@code FieldError} de Spring para extraer
         * automáticamente el nombre del campo y el mensaje de error.
         * @param error El objeto FieldError.
         */
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
