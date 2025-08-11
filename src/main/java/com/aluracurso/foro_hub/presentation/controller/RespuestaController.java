package com.aluracurso.foro_hub.presentation.controller;

import com.aluracurso.foro_hub.aplication.dto.ActualizarRespuestaDTO;
import com.aluracurso.foro_hub.aplication.dto.DatosCrearRespuestaDTO;
import com.aluracurso.foro_hub.aplication.dto.DetalleRespuestaDTO;
import com.aluracurso.foro_hub.domain.respuesta.Respuesta;
import com.aluracurso.foro_hub.aplication.service.RespuestaService;
import com.aluracurso.foro_hub.domain.topico.Topico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuestas", description = "Endpoints para la gestión de respuestas a tópicos")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    /**
     * Endpoint para crear una nueva respuesta.
     * Usa el DTO CrearRespuestaDTO para recibir los datos del cliente.
     * @param DatosCrearRespuestaDTO DTO con los datos de la nueva respuesta.
     * @return ResponseEntity con la respuesta creada y el código de estado 201.
     */
    @PostMapping
    @Operation(summary = "Crea una nueva respuesta", description = "Registra una nueva respuesta a un tópico existente.")
    @ApiResponse(responseCode = "201", description = "Respuesta creada exitosamente", content = @Content(schema = @Schema(implementation = DetalleRespuestaDTO.class)))
    @ApiResponse(responseCode = "400", description = "El cuerpo de la solicitud no es válido o hay un error de validación.")
    @ApiResponse(responseCode = "404", description = "El tópico o el autor especificado no existe.")
    public ResponseEntity<DetalleRespuestaDTO> crearRespuesta(@RequestBody @Valid DatosCrearRespuestaDTO datosCrearRespuestaDTO) {
        Respuesta nuevaRespuesta = new Respuesta();
        nuevaRespuesta.setMensaje(datosCrearRespuestaDTO.mensaje());
        nuevaRespuesta.setTopico(new Topico(datosCrearRespuestaDTO.topicoId()));
        nuevaRespuesta.setFechaCreacion(LocalDateTime.now());
        Optional<Respuesta> respuestaGuardada = respuestaService.guardar(nuevaRespuesta);

        return respuestaGuardada.map(respuesta -> {
            DetalleRespuestaDTO detalleRespuesta = new DetalleRespuestaDTO(
                    respuesta.getId(),
                    respuesta.getMensaje(),
                    respuesta.getTopico().getId(),
                    respuesta.getFechaCreacion(),
                    respuesta.getSolucion()
            );
            return new ResponseEntity<>(detalleRespuesta, HttpStatus.CREATED);
        }).orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Endpoint para listar las respuestas de un tópico específico.
     * @param topicoId El ID del tópico.
     * @return Una lista de DTOs de las respuestas del tópico.
     */
    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Lista respuestas por tópico", description = "Obtiene todas las respuestas asociadas a un tópico específico.")
    @ApiResponse(responseCode = "200", description = "Respuestas listadas exitosamente.", content = @Content(schema = @Schema(implementation = DetalleRespuestaDTO.class)))
    @ApiResponse(responseCode = "404", description = "El tópico con el ID especificado no existe.")
    public ResponseEntity<List<DetalleRespuestaDTO>> listarRespuestasPorTopico(@PathVariable Long topicoId) {
        List<Respuesta> respuestas = respuestaService.listarPorTopico(topicoId);
        List<DetalleRespuestaDTO> detalleRespuestas = respuestas.stream()
                .map(respuesta -> new DetalleRespuestaDTO(
                        respuesta.getId(),
                        respuesta.getMensaje(),
                        respuesta.getTopico().getId(),
                        respuesta.getFechaCreacion(),
                        respuesta.getSolucion()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(detalleRespuestas, HttpStatus.OK);
    }

    /**
     * Endpoint para actualizar una respuesta existente.
     * Usa el DTO ActualizarRespuestaDTO para recibir los datos.
     * @param id El ID de la respuesta a actualizar.
     * @param actualizarRespuestaDTO DTO con los datos actualizados.
     * @return ResponseEntity con la respuesta actualizada o 404 si no se encuentra.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una respuesta existente", description = "Actualiza el mensaje de una respuesta por su ID.")
    @ApiResponse(responseCode = "200", description = "Respuesta actualizada exitosamente.", content = @Content(schema = @Schema(implementation = DetalleRespuestaDTO.class)))
    @ApiResponse(responseCode = "403", description = "Acceso no autorizado: el usuario no es el autor de la respuesta ni un administrador.")
    @ApiResponse(responseCode = "404", description = "La respuesta con el ID especificado no existe.")
    public ResponseEntity<DetalleRespuestaDTO> actualizarRespuesta(@PathVariable Long id, @RequestBody ActualizarRespuestaDTO actualizarRespuestaDTO) {
        Respuesta respuestaActualizada = new Respuesta();
        Optional<Respuesta> resultado = respuestaService.actualizar(id, actualizarRespuestaDTO.mensaje());

        return resultado.map(respuesta -> {
            DetalleRespuestaDTO detalleRespuesta = new DetalleRespuestaDTO(
                    respuesta.getId(),
                    respuesta.getMensaje(),
                    respuesta.getTopico().getId(),
                    respuesta.getFechaCreacion(),
                    respuesta.getSolucion()
            );
            return new ResponseEntity<>(detalleRespuesta, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint para marcar una respuesta como solución.
     * @param id El ID de la respuesta a marcar como solución.
     * @return ResponseEntity con la respuesta actualizada o 404 si no se encuentra.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @PutMapping("/{id}/solucion")
    @Operation(summary = "Marca una respuesta como solución", description = "Establece una respuesta como la solución de un tópico.")
    @ApiResponse(responseCode = "200", description = "Respuesta marcada como solución exitosamente.", content = @Content(schema = @Schema(implementation = DetalleRespuestaDTO.class)))
    @ApiResponse(responseCode = "400", description = "Petición inválida: La respuesta ya ha sido marcada como la solución del tópico.")
    @ApiResponse(responseCode = "404", description = "La respuesta con el ID especificado no existe.")
    @ApiResponse(responseCode = "403", description = "Acceso no autorizado: el usuario no es el autor de la respuesta ni un administrador.")
    public ResponseEntity<DetalleRespuestaDTO> marcarComoSolucion(@PathVariable Long id) {
        Optional<Respuesta> resultado = respuestaService.marcarComoSolucion(id);

        return resultado.map(respuesta -> {
            DetalleRespuestaDTO detalleRespuesta = new DetalleRespuestaDTO(
                    respuesta.getId(),
                    respuesta.getMensaje(),
                    respuesta.getTopico().getId(),
                    respuesta.getFechaCreacion(),
                    respuesta.getSolucion()
            );
            return new ResponseEntity<>(detalleRespuesta, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Endpoint para eliminar una respuesta.
     * @param id El ID de la respuesta a eliminar.
     * @return ResponseEntity sin contenido y código 204.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una respuesta", description = "Elimina una respuesta existente por su ID.")
    @ApiResponse(responseCode = "204", description = "Respuesta eliminada exitosamente.")
    @ApiResponse(responseCode = "404", description = "La respuesta con el ID especificado no existe.")
    @ApiResponse(responseCode = "403", description = "Acceso no autorizado: el usuario no es el autor de la respuesta ni un administrador.")
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Long id) {
        if (respuestaService.eliminar(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}