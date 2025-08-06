#----------------------------------------------------------------------
# Etapa 1: Construir la aplicación Spring Boot
# Esta etapa utiliza un JDK para compilar el código y generar el JAR.
#----------------------------------------------------------------------
FROM maven:3.8.7-openjdk-17-slim AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo pom.xml y descarga las dependencias de Maven.
# Esto mejora el caché de la capa de Docker si el pom.xml no cambia.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto de los archivos del proyecto
COPY src ./src

# Empaqueta la aplicación en un archivo JAR
RUN mvn package

#----------------------------------------------------------------------
# Etapa 2: Crear la imagen final de la aplicación
# Esta etapa utiliza un JRE más ligero para ejecutar la aplicación.
# Se copia solo el JAR de la etapa de construcción anterior.
#----------------------------------------------------------------------
FROM openjdk:17-jre-slim

# Establece un argumento para el nombre del archivo JAR
ARG JAR_FILE=target/foro-hub-auth-service-0.0.1-SNAPSHOT.jar

# Copia el archivo JAR desde la etapa de construcción (builder)
COPY --from=builder /app/${JAR_FILE} app.jar

# Expone el puerto por defecto de Spring Boot (8080)
EXPOSE 8080

# Define el comando para ejecutar la aplicación cuando el contenedor se inicie
ENTRYPOINT ["java", "-jar", "/app.jar"]
