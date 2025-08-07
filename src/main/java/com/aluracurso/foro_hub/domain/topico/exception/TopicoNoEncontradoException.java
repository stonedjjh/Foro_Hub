package com.aluracurso.foro_hub.domain.topico.exception; // Ubicación recomendada en la capa de dominio

public class TopicoNoEncontradoException extends RuntimeException { // Extiende RuntimeException para ser unchecked

    public TopicoNoEncontradoException(String message) {
        super(message); // Llama al constructor de la clase padre (RuntimeException)
    }

    // Opcional: Puedes añadir un constructor que acepte una causa (otra excepción)
    public TopicoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
