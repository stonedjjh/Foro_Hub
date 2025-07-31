package com.aluracurso.Foro_Hub.presentation.controller;

import com.aluracurso.Foro_Hub.aplication.dto.DatosTopicoDTO;
import com.aluracurso.Foro_Hub.aplication.dto.TopicoActualizacionDTO;
import com.aluracurso.Foro_Hub.aplication.dto.TopicoDTO;
import com.aluracurso.Foro_Hub.aplication.service.TopicoApplicationService;
import com.aluracurso.Foro_Hub.domain.entity.Topico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

//importaciones necesarias para trabajar con paginación
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoApplicationService topicoApplicationService;

    private List<DatosTopicoDTO>  retornarLista(Optional<List<DatosTopicoDTO>> listaDatosTopicosDTO ){
        if (listaDatosTopicosDTO.isPresent())
        {
            return listaDatosTopicosDTO.get();
        }
        return null;
    }



    @GetMapping // Este método ahora manejará la paginación
    public Page<DatosTopicoDTO> listarTopicos(@PageableDefault(size = 10, sort = {"fechaCreacion"}) Pageable paginacion){
        // Llama al servicio con el objeto Pageable
        return topicoApplicationService.obtenerTopicos(paginacion);
    }

    @GetMapping("/primeros10topicos")
    public List<DatosTopicoDTO> listarPrimeros10topicos(){
        return this.retornarLista(topicoApplicationService.obtener10Topicos());
    }

    @GetMapping("/buscar")
    public List<DatosTopicoDTO>buscarTopicosPorCriterios(
            @RequestParam String titulo,
            @RequestParam int anio
    ) {
        return this.retornarLista(topicoApplicationService.buscarTituloyAnio(titulo,anio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatosTopicoDTO> actualizarTopicoPorId(@PathVariable Long id, @Valid @RequestBody TopicoActualizacionDTO topicoActualizacionDTO){
        DatosTopicoDTO datosTopicoDTO = topicoApplicationService.actualizarTopico(id,topicoActualizacionDTO);
        return ResponseEntity.ok(datosTopicoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> elminarTopicoPorId(@PathVariable Long id){
        topicoApplicationService.eliminarTopico(id);
        return ResponseEntity.ok("Tópico eliminado");
    }


    @GetMapping("/{id}")
    public ResponseEntity<DatosTopicoDTO> buscarTopicoPorId(@PathVariable Long id) {
        DatosTopicoDTO datosTopicoDTO = topicoApplicationService.obtenerTopicoPorId(id);
        return ResponseEntity.ok(datosTopicoDTO);
    }

    @PostMapping
    public ResponseEntity<DatosTopicoDTO> agregarNuevoTopico(@Valid @RequestBody TopicoDTO topicoDTO){
        Topico topico = new Topico(topicoDTO);
        Topico topicoAlmacenado = topicoApplicationService.guardarTopico(topico, topicoDTO.curso());
        return ResponseEntity.status(HttpStatus.CREATED).body(new DatosTopicoDTO(topicoAlmacenado));
    }
}
