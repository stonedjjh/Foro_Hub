package com.aluracurso.foro_hub.infrastructure.repository;

import com.aluracurso.foro_hub.infrastructure.persistence.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IJpaTopicoRepository extends JpaRepository<Topico,Long> {

    List<Topico> findTop10ByOrderByFechaCreacionDesc();

    /**
     * Busca todos los tópicos que coinciden con un título y un año específicos.
     * <p>
     * Se usa una consulta JPQL personalizada con la anotación @Query
     * para extraer el año del campo `fechaCreacion` y compararlo con el parámetro.
     * La función `YEAR()` es una función estándar de JPQL que se usa para esto.
     * </p>
     *
     * @param titulo El título (o parte de él) a buscar.
     * @param anio   El año de creación del tópico.
     * @return Una lista de tópicos que cumplen con los criterios.
     */
    @Query("SELECT t FROM Topico t WHERE UPPER(t.titulo) LIKE %:nombre%o AND YEAR(t.fechaCreacion) = :anio")
    List<Topico> buscarTopicosPorTituloYAnio(String titulo, int anio);
}
