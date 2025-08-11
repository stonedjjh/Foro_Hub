package com.aluracurso.foro_hub.aplication.dto;

import java.time.LocalDateTime;

/**
 * DTO para la representación detallada de una respuesta.
 * Se utiliza para enviar la información al cliente después de una operación.
 *
 * @param id El ID de la respuesta.
 * @param mensaje El contenido de la respuesta.
 * @param topicoId El ID del tópico al que pertenece la respuesta.
 * @param fechaCreacion La fecha y hora de creación de la respuesta.
 * @param solucion Indica si la respuesta fue marcada como solución.
 */
public record DetalleRespuestaDTO(
        Long id,
        String mensaje,
        Long topicoId,
        LocalDateTime fechaCreacion,
        Boolean solucion) {
}
