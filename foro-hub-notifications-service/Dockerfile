# Usa una imagen base de OpenJDK para Java 17.
# 'alpine' es una distribución ligera, ideal para imágenes de Docker pequeñas.
FROM openjdk:17-jdk-slim-buster

# Establece el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor.
# Asume que tu aplicación Spring Boot genera un JAR ejecutable en la carpeta 'target'.
# Asegúrate de que el nombre del JAR coincida con el que genera tu proyecto Maven.
# Por ejemplo: foro-hub-notifications-service-0.0.1-SNAPSHOT.jar
COPY target/foro-hub-notifications-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que la aplicación Spring Boot se ejecutará.
# Por defecto, Spring Boot usa el puerto 8080.
EXPOSE 8080

# Define el comando que se ejecutará cuando el contenedor se inicie.
# Esto iniciará tu aplicación Spring Boot.
ENTRYPOINT ["java", "-jar", "app.jar"]
