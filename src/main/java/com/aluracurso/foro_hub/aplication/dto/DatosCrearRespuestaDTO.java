package com.aluracurso.foro_hub.aplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para la petición de creación de una nueva respuesta.
 * Contiene solo la información esencial que el cliente debe proporcionar.
 * El ID del autor se obtendrá del usuario autenticado en el servidor.
 *
 * @param mensaje El contenido de la respuesta.
 * @param topicoId El ID del tópico al que pertenece la respuesta.
 */
@Schema(description = "DTO para la creación de una nueva respuesta en el foro.")
public record DatosCrearRespuestaDTO(
        @NotBlank(message = "El mensaje no puede estar vacío.")
        @Schema(description = "El contenido de la respuesta.", example = "Muy buena pregunta, estoy de acuerdo con tu análisis")
        String mensaje,

        @NotNull(message = "El ID del tópico no puede ser nulo.")
        @Schema(description = "El ID del tópico al que se asocia esta respuesta.", example = "1")
        Long topicoId) {
}
