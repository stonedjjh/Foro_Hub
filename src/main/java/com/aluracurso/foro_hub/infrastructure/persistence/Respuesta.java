package com.aluracurso.foro_hub.infrastructure.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidad que representa una respuesta en la base de datos.
 * Esta clase se mapea a la tabla "respuestas".
 * Utiliza Lombok para generar automáticamente getters, setters,
 * constructores sin argumentos y con todos los argumentos.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "respuestas")
public class Respuesta {

    /**
     * El ID único de la respuesta.
     * Es la clave primaria de la tabla y se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * El mensaje de la respuesta.
     * La anotación @NotBlank valida que el campo no sea nulo, ni vacío, ni solo espacios en blanco.
     */
    @NotBlank
    private String mensaje;

    /**
     * Relación Many-to-One con la entidad Topico.
     * Cada respuesta pertenece a un solo tópico.
     * La anotación @JoinColumn especifica la columna de la clave foránea.
     * fetch = FetchType.LAZY indica que el tópico se cargará solo cuando se acceda a él.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico", nullable = false)
    private Topico topico;

    /**
     * Relación Many-to-One con la entidad Usuario.
     * Cada respuesta tiene un único autor (usuario).
     * fetch = FetchType.LAZY indica que el autor se cargará solo cuando se acceda a él.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor", nullable = false)
    private Usuario autor;

    /**
     * Indica si la respuesta es la solución al tópico.
     * La anotación @NotNull valida que el campo no sea nulo.
     */
    @NotNull
    private Boolean solucion;

    /**
     * La fecha de creación de la respuesta.
     * Esta columna es gestionada por la base de datos con un valor por defecto (e.g., NOW()).
     * La anotación @Column(insertable = false, updatable = false) le dice a JPA que no incluya
     * este campo en las sentencias INSERT y UPDATE. Esto es una buena práctica para
     * evitar conflictos con los valores generados por la base de datos.
     */
    @Column(insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Override
    public String toString() {
        return "Respuesta{" +
                "id=" + id +
                ", mensaje='" + mensaje + '\'' +
                ", topico=" + topico +
                ", autor=" + autor +
                ", solucion=" + solucion +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }


}
