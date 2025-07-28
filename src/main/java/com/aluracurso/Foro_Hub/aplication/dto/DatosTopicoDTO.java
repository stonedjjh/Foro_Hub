package com.aluracurso.Foro_Hub.aplication.dto;

import com.aluracurso.Foro_Hub.domain.topico.entity.Topico;

import java.time.LocalDateTime;

public record DatosTopicoDTO(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String status,
        Integer autor,
        Integer curso
) {
    public DatosTopicoDTO(Topico topico) {
        this(topico.getId(), topico.getTitulo(),topico.getMensaje(),topico.getFechaCreacion(),
                topico.getStatus(),topico.getAutor(),topico.getCurso());
    }

}
