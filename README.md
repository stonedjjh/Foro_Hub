# Foro_Hub 💬 - API REST para Gestión de Tópicos

Foro_Hub es una API REST desarrollada en Java con Spring Boot, diseñada para la gestión de tópicos de discusión. Permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los tópicos, incluyendo funcionalidades de listado con paginación y búsqueda, así como un manejo robusto de excepciones. El proyecto sigue principios de diseño de API REST y buenas prácticas de desarrollo.

---

🚀 **Tecnologías utilizadas**

* **Java 17**
* **Spring Boot 3.5.4**
* **Maven**
* **MySQL 10.4.32-MariaDB**
* **JPA + Spring Data**
* **Flyway** (para migraciones de base de datos)
* **Lombok** (para reducir código boilerplate)
* **Jakarta Validation** (para validación de datos)
* **Spring Security** (para autenticación y autorización)
* **Auth0 JWT (java-jwt)** (para generación y validación de tokens JWT)
* **Insomnia** (para pruebas de API)

---

📁 **Estructura del proyecto**

La organización del proyecto se adhiere a una estructura por capas, separando las responsabilidades para una mejor mantenibilidad y claridad:
```src/
└── main/
├── java/com/aluracurso/Foro_Hub/
│   ├── aplication/         # DTOs y Servicios de Aplicación
│   │   ├── dto             # Data Transfer Objects (DTOs) para entrada/salida
│   │   └── service         # Lógica de negocio y orquestación
│   ├── domain/             # Entidades de Dominio y Repositorios
│   │   └── topico          # Relacionado con la entidad Tópico
│   │       ├── entity      # Definición de la entidad Tópico
│   │       ├── exception   # Excepciones personalizadas del dominio
│   │       └── repository  # Interfaces de Repositorio JPA
│   ├── infrastructure/     # Configuración y Manejo de Excepciones Globales
│   │   ├── config          # Configuraciones generales, incluyendo seguridad (JWT, filtros)
│   │   └── exception       # Manejadores de excepciones globales (@ControllerAdvice)
│   └── presentation/       # Controladores de la API REST
│       └── controller      # Endpoints de la API
└── resources/              # Archivos de configuración y migraciones Flyway
└── db
└── migration
```

✅ **Funcionalidades implementadas**

1.  **Configuración inicial y persistencia de datos:**
    * Proyecto Maven con dependencias esenciales.
    * Uso de Flyway para gestionar migraciones de base de datos, incluyendo la creación de tablas y precarga de datos (`curso`, `usuario`, `perfil`).

2.  **Registro de un nuevo Tópico (POST):**
    * Endpoint: `POST /topicos`
    * Permite crear un nuevo tópico validando los datos de entrada.
    * Manejo de excepción para tópicos duplicados (título y mensaje).

3.  **Listado de Tópicos (GET):**
    * **Listar todos con paginación:** `GET /topicos`
        * Soporta paginación (`size`, `page`, `sort`) con `@PageableDefault`.
    * **Listar los primeros 10:** `GET /topicos/primeros10topicos`
        * Obtiene los 10 tópicos más recientes ordenados por fecha de creación.
    * **Búsqueda por criterios:** `GET /topicos/buscar?titulo={titulo}&anio={anio}`
        * Permite buscar tópicos por una parte del título y por el año de creación.

4.  **Detalle de un Tópico (GET por ID):**
    * Endpoint: `GET /topicos/{id}`
    * Muestra la información detallada de un tópico específico.
    * Manejo de excepción `404 Not Found` si el tópico no existe.

5.  **Actualización de un Tópico (PUT):**
    * Endpoint: `PUT /topicos/{id}`
    * Permite actualizar los datos de un tópico existente (título, mensaje, status, curso).
    * Validaciones aplicadas a los datos de entrada.
    * Manejo de excepciones para tópicos no encontrados o datos duplicados.

6.  **Eliminación de un Tópico (DELETE):**
    * Endpoint: `DELETE /topicos/{id}`
    * Elimina un tópico de la base de datos por su ID.
    * Manejo de excepción `404 Not Found` si el tópico no existe antes de intentar eliminarlo.

7.  **Manejo de Excepciones Global:**
    * Implementación de `TopicoDuplicadoException` y `TopicoNoEncontradoException` para manejar errores de negocio de forma específica.
    * Configuración de un manejador de excepciones global (`@ControllerAdvice`) para mapear estas excepciones a códigos de estado HTTP apropiados (ej. `409 Conflict`, `404 Not Found`). Ahora también maneja errores de autenticación como `401 Unauthorized`.

8.  **Autenticación y Control de Acceso con JWT:**
    * **Endpoint de Login:** `POST /login` para autenticar usuarios y generar un token JWT.
    * **Generación de Tokens JWT:** Creación de tokens JWT seguros (HMAC256) con información del emisor y fecha de expiración.
    * **Validación de Tokens:** Implementación de un `SecurityFilter` personalizado para interceptar y validar el token JWT en cada solicitud protegida.
    * **Carga de Usuario:** El filtro utiliza el "subject" del token para cargar los detalles del usuario desde la base de datos y establecer la autenticación en el `SecurityContextHolder`.
    * **Configuración de Seguridad:** Spring Security configurado para ser sin estado (`STATELESS`), deshabilitar CSRF, permitir el acceso a `/login` y requerir autenticación para todas las demás rutas.
    * **Encriptación de Contraseñas:** Uso de `BCryptPasswordEncoder` para almacenar contraseñas de forma segura en la base de datos.

---

🖥️ **Cómo ejecutar**

1.  **Requisitos previos:**
    * Asegúrate de tener Java 17 o superior instalado.
    * Tener Maven instalado.
    * Asegúrate de tener una instancia de **MySQL (MariaDB 10.4.32+)** en funcionamiento.

2.  **Configuración de la Base de Datos:**
    * Crea una base de datos para el proyecto (el nombre se configura en `src/main/resources/application.properties`).
    * Configura las credenciales de conexión a tu base de datos en el archivo `src/main/resources/application.properties`.

3.  **Configuración de la Clave Secreta JWT:**
    * Genera una clave secreta segura (min. 32 bytes) para JWT. Puedes usar el siguiente comando en PowerShell:
        ```powershell
        $bytes = New-Object byte[] 32; [System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes); [System.Convert]::ToBase64String($bytes)
        ```
    * Añade esta clave a tu `src/main/resources/application.properties` con la propiedad:
        ```properties
        api.security.token.secret=TU_CLAVE_SECRETA_GENERADA_AQUI
        ```

4.  **Ejecución:**
    * Clona este repositorio: `git clone https://github.com/stonedjjh/Foro_Hub.git`
    * Navega a la raíz del proyecto.
    * **Desde IntelliJ IDEA:** Abre el proyecto con IntelliJ. Localiza la clase principal `ForoHubApplication.java` y ejecútala.
    * **Desde la línea de comandos:**
        ```bash
        ./mvnw spring-boot:run
        ```

La API estará disponible en `http://localhost:8080/` (o el puerto configurado). Puedes usar herramientas como Insomnia o Postman para interactuar con los endpoints.

5.  **Prueba de Autenticación (Obtener Token JWT):**
    Una vez que la aplicación esté corriendo, puedes obtener un token JWT utilizando el siguiente usuario de prueba que se inserta con Flyway:

    * **Usuario de Prueba:**
        * **Nombre:** `test_user`
        * **Correo Electrónico:** `test@test.com`
        * **Contraseña:** `12345678` (ya cifrada en la base de datos)

    * **JSON para la solicitud POST a `http://localhost:8080/login`:**
        ```json
        {
          "correoElectronico": "test@test.com",
          "clave": "12345678"
        }
        ```
    Al enviar esta solicitud, recibirás un token JWT que podrás usar para acceder a las rutas protegidas.

---

📌 **Estado actual**

✅ 100% de las funcionalidades CRUD (Crear, Leer, Actualizar, Eliminar) implementadas para tópicos.
✅ Persistencia funcional con MySQL (MariaDB) y migraciones con Flyway.
✅ Aplicación de principios REST y buenas prácticas de desarrollo en Spring Boot.
✅ Manejo de excepciones de negocio y mapeo a códigos HTTP apropiados.
✅ Implementación completa de autenticación y control de acceso utilizando JWT y Spring Security.
✅ Configuración de seguridad para APIs REST (sin estado, protección de endpoints, encriptación de contraseñas).

---

📎 **Repositorio**

El código fuente se encuentra disponible en GitHub:
[https://github.com/stonedjjh/Foro_Hub](https://github.com/stonedjjh/Foro_Hub)

---

📄 **Licencia**

Proyecto desarrollado con fines educativos como parte de un curso de backend de Alura Latam.

---

👤 **Autor**

Daniel Joel Jiménez Herrera
GitHub: [@stonedjjh](https://github.com/stonedjjh)