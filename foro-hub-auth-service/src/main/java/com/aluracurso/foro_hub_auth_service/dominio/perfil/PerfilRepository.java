package com.aluracurso.foro_hub_auth_service.dominio.perfil;

import java.util.Optional;

public interface PerfilRepository {
    Perfil guardar(Perfil perfil);

    Optional<Perfil> encontrarPorNombre(String nombre);
    Optional<Perfil> encontrarPorId(Long Id);
}
