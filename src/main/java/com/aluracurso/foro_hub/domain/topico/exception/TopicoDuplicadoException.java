package com.aluracurso.foro_hub.domain.topico.exception;

public class TopicoDuplicadoException extends RuntimeException {
    public TopicoDuplicadoException(String message) {
        super(message); // Llama al constructor de la clase padre (RuntimeException)
    }

    // Opcional: Puedes añadir un constructor que acepte una causa (otra excepción)
    public TopicoDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}


