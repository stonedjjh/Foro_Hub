package com.aluracurso.foro_hub.domain.perfil;

import com.aluracurso.foro_hub.domain.respuesta.Respuesta;

import java.util.Optional;

public interface PerfilRepository {
    Perfil guardar(Perfil perfil);
    Optional<Perfil> encontrarPorId(Long Id);

}
