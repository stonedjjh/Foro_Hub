package com.aluracurso.foro_hub.domain.perfil;

import java.util.Optional;

public interface PerfilRepository {
    Perfil guardar(Perfil perfil);

    Optional<Perfil> encontrarPorNombre(String nombre);
    Optional<Perfil> encontrarPorId(Long Id);
}
