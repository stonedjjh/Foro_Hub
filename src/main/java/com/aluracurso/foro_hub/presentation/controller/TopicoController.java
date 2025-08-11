package com.aluracurso.foro_hub.presentation.controller;

import com.aluracurso.foro_hub.aplication.dto.DatosTopicoDTO;
import com.aluracurso.foro_hub.aplication.dto.TopicoActualizacionDTO;
import com.aluracurso.foro_hub.aplication.dto.TopicoDTO;
import com.aluracurso.foro_hub.aplication.service.TopicoApplicationService;
import com.aluracurso.foro_hub.domain.topico.Topico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de tópicos.
 * Provee endpoints para la creación, consulta, actualización y eliminación de tópicos.
 * La documentación de la API es generada con OpenAPI (Swagger).
 */
@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Tópicos", description = "Endpoints para la gestión de tópicos del foro")
public class TopicoController {

    @Autowired
    private TopicoApplicationService topicoApplicationService;

    /**
     * Método auxiliar para retornar una lista de tópicos de manera segura,
     * evitando un posible NullPointerException si el Optional está vacío.
     * @param listaDatosTopicosDTO Un Optional que puede contener una lista de DTOs de tópicos.
     * @return Una lista de DTOs de tópicos o una lista vacía si el Optional está vacío.
     */
    private List<DatosTopicoDTO> retornarLista(Optional<List<DatosTopicoDTO>> listaDatosTopicosDTO) {
        return listaDatosTopicosDTO.orElse(Collections.emptyList());
    }

    /**
     * Obtiene una lista paginada de todos los tópicos.
     *
     * @param paginacion Objeto Pageable para controlar la paginación (tamaño, número de página, orden).
     * @return Una página de DTOs de tópicos con un estado HTTP 200 OK.
     */
    @GetMapping
    @Operation(summary = "Obtiene una lista paginada de tópicos",
            description = "Este endpoint devuelve una lista de tópicos con paginación y ordenamiento.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de tópicos obtenida exitosamente")
            })
    public Page<DatosTopicoDTO> listarTopicos(
            @PageableDefault(size = 10, sort = {"fechaCreacion"}) Pageable paginacion) {
        return topicoApplicationService.obtenerTopicos(paginacion);
    }

    /**
     * Obtiene los 10 tópicos más recientes.
     *
     * @return Una lista de DTOs de los 10 tópicos más recientes.
     */
    @GetMapping("/topicosmasrecientes")
    @Operation(summary = "Obtiene los 10 tópicos más recientes",
            description = "Devuelve una lista de los 10 primeros tópicos ordenados por fecha de creación de manera descendente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de los 10 tópicos más recientes obtenida exitosamente")
            })
    public List<DatosTopicoDTO> listarPrimeros10topicos() {
        return this.retornarLista(topicoApplicationService.obtener10Topicos());
    }

    /**
     * Busca tópicos por título y año.
     *
     * @param titulo El título o parte del título del tópico a buscar.
     * @param anio El año de creación del tópico.
     * @return Una lista de DTOs de los tópicos encontrados.
     */
    @GetMapping("/buscar")
    @Operation(summary = "Busca tópicos por título y año",
            description = "Permite buscar tópicos que coincidan con un título y un año de creación específicos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tópicos encontrados exitosamente")
            })
    public List<DatosTopicoDTO> buscarTopicosPorCriterios(
            @Parameter(description = "Título o parte del título del tópico") @RequestParam String titulo,
            @Parameter(description = "Año de creación del tópico") @RequestParam int anio
    ) {
        return this.retornarLista(topicoApplicationService.buscarTituloyAnio(titulo, anio));
    }

    /**
     * Actualiza un tópico existente por su ID.
     *
     * @param id El ID del tópico a actualizar.
     * @param topicoActualizacionDTO El DTO con los datos para la actualización.
     * @return ResponseEntity con los datos del tópico actualizado y un estado HTTP 200 OK.
     */

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un tópico por su ID",
            description = "Permite a un usuario actualizar el título o el mensaje de un tópico existente que haya creado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tópico actualizado exitosamente",
                            content = @Content(schema = @Schema(implementation = DatosTopicoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "El tópico con el ID especificado no fue encontrado")
            })
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<DatosTopicoDTO> actualizarTopicoPorId(
            @Parameter(description = "ID del tópico a actualizar") @PathVariable Long id,
            @Valid @RequestBody TopicoActualizacionDTO topicoActualizacionDTO) {
        DatosTopicoDTO datosTopicoDTO = topicoApplicationService.actualizarTopico(id, topicoActualizacionDTO);
        return ResponseEntity.ok(datosTopicoDTO);
    }

    /**
     * Elimina un tópico del sistema por su ID.
     *
     * @param id El ID del tópico a eliminar.
     * @return ResponseEntity con un mensaje de éxito y un estado HTTP 200 OK.
     */


    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un tópico por su ID",
            description = "Elimina un tópico de la base de datos de manera lógica, marcándolo como inactivo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tópico eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "El tópico con el ID especificado no fue encontrado")
            })
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == principal.id")
    public ResponseEntity<String> elminarTopicoPorId(
            @Parameter(description = "ID del tópico a eliminar") @PathVariable Long id) {
        topicoApplicationService.eliminarTopico(id);
        return ResponseEntity.ok("Tópico eliminado");
    }

    /**
     * Busca y obtiene los detalles de un tópico por su ID.
     *
     * @param id El ID del tópico a buscar.
     * @return ResponseEntity con los datos del tópico encontrado y un estado HTTP 200 OK.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Busca un tópico por su ID",
            description = "Devuelve los detalles de un tópico específico según su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tópico encontrado exitosamente",
                            content = @Content(schema = @Schema(implementation = DatosTopicoDTO.class))),
                    @ApiResponse(responseCode = "404", description = "El tópico con el ID especificado no fue encontrado")
            })
    public ResponseEntity<DatosTopicoDTO> buscarTopicoPorId(
            @Parameter(description = "ID del tópico a buscar") @PathVariable Long id) {
        DatosTopicoDTO datosTopicoDTO = topicoApplicationService.obtenerTopicoPorId(id);
        return ResponseEntity.ok(datosTopicoDTO);
    }

    /**
     * Crea un nuevo tópico.
     *
     * @param topicoDTO El DTO con los datos del nuevo tópico.
     * @return ResponseEntity con el tópico creado y un estado HTTP 201 Created.
     */
    @PostMapping
    @Operation(summary = "Crea un nuevo tópico",
            description = "Crea un nuevo tópico en la base de datos del foro.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tópico creado exitosamente",
                            content = @Content(schema = @Schema(implementation = DatosTopicoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "El curso o el usuario no existen",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflicto: El tópico ya existe (título y mensaje duplicados)")
            })
    public ResponseEntity<DatosTopicoDTO> agregarNuevoTopico(@Valid @RequestBody TopicoDTO topicoDTO) {
        Topico topico = new Topico(topicoDTO);
        Topico topicoAlmacenado = topicoApplicationService.guardarTopico(topico);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DatosTopicoDTO(topicoAlmacenado));
    }
}
