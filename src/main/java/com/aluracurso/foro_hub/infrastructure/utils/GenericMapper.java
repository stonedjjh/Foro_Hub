package com.aluracurso.foro_hub.infrastructure.utils;

import java.lang.reflect.Field;
import java.util.stream.Stream;

// Una clase de utilidad estática, ideal para métodos que no necesitan estado.
// No se necesita crear una instancia de esta clase.
public final class GenericMapper {

    private GenericMapper() {
        // Constructor privado para evitar que la clase sea instanciada.
    }

    /**
     * Mapea los atributos de un objeto origen a un objeto destino usando Reflexión.
     * Los atributos deben tener el mismo nombre y ser del mismo tipo para ser copiados.
     * Este es un método de ejemplo, se recomienda usar librerías de mapeo en producción.
     *
     * @param origen  El objeto del que se copiarán los atributos.
     * @param destino El objeto en el que se copiarán los atributos.
     * @param <S>     El tipo del objeto origen.
     * @param <D>     El tipo del objeto destino.
     */
    public static <S, D> void map(S origen, D destino) {
        // Aseguramos que ambos objetos no sean nulos
        if (origen == null || destino == null) {
            return;
        }

        // Obtiene todos los campos del objeto de origen, incluidos los privados
        Field[] camposOrigen = origen.getClass().getDeclaredFields();

        // Itera sobre los campos del origen
        Stream.of(camposOrigen).forEach(campoOrigen -> {
            try {
                // Permite el acceso a campos privados
                campoOrigen.setAccessible(true);

                // Busca un campo con el mismo nombre en el objeto destino
                Field campoDestino = destino.getClass().getDeclaredField(campoOrigen.getName());

                // Permite el acceso a campos privados en el destino
                campoDestino.setAccessible(true);

                // Copia el valor del campo del origen al destino
                Object valor = campoOrigen.get(origen);
                campoDestino.set(destino, valor);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Si el campo no existe en el destino, o no se puede acceder,
                // simplemente lo ignoramos y continuamos con el siguiente.
                // Es importante manejar las excepciones de forma controlada.
                System.err.println("No se pudo mapear el campo: " + campoOrigen.getName() + " debido a: " + e.getMessage());
            }
        });
    }
}
