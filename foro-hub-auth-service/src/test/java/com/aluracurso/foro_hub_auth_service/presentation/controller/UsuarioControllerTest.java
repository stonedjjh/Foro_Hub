package com.aluracurso.foro_hub_auth_service.presentation.controller;

import com.aluracurso.foro_hub_auth_service.aplicacion.dto.DatosActualizarUsuarioDTO;
import com.aluracurso.foro_hub_auth_service.aplicacion.service.UsuarioService;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.infraestructura.config.TestSecurityConfiguration;
import com.aluracurso.foro_hub_auth_service.infraestructura.config.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// Importamos la configuración de seguridad para que @PreAuthorize funcione en el test
@Import(TestSecurityConfiguration.class)
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private TokenService tokenService;


    /**
     * Prueba el caso de éxito: un usuario con rol ADMINISTRADOR
     * puede acceder y se retorna una página de usuarios.
     */
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testListarUsuarios_retornaPaginaDeUsuarios() throws Exception {
        // 1. Configuración de los datos de prueba
        List<Usuario> listaUsuarios = List.of(
                new Usuario(
                        "Usuario de Prueba 1",
                        "usuario1@ejemplo.com",
                        "contraseña_hash_1",
                        List.of(new Perfil(1L, "ROLE_USUARIO"))
                ),
                new Usuario(
                        "Usuario de Prueba 2",
                        "usuario2@ejemplo.com",
                        "contraseña_hash_2",
                        List.of(new Perfil(2L, "ROLE_ADMINISTRADOR"))
                )
        );

        Page<Usuario> paginaUsuarios = new PageImpl<>(listaUsuarios, PageRequest.of(0, 10), 2);

        // 2. Comportamiento del "mock"
        when(usuarioService.listar(any())).thenReturn(paginaUsuarios);

        // 3. Ejecución de la prueba
        mockMvc.perform(get("/usuario")
                        .contentType(MediaType.APPLICATION_JSON))
                // 4. Verificación de los resultados
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nombre").value("Usuario de Prueba 1"))
                .andExpect(jsonPath("$.content[1].correoElectronico").value("usuario2@ejemplo.com"));
    }

    /**
     * Prueba el caso de fallo: un usuario con rol USUARIO intenta acceder
     * a un recurso protegido para ADMINISTRADOR y se deniega el acceso.
     */

    @Test
    @WithMockUser(roles = "USUARIO")
    void testListarUsuarios_retorna403ForbiddenParaUsuarioSinPermisos() throws Exception {
        // Ejecutamos la llamada HTTP GET simulada al endpoint /usuario
        // con un usuario que tiene el rol "USUARIO", que no es suficiente para acceder.
        mockMvc.perform(get("/usuario")
                        .contentType(MediaType.APPLICATION_JSON))
                // Esperamos que la respuesta sea 403 Forbidden.
                .andExpect(status().isForbidden());
    }


    /**
     * Prueba el caso de fallo: un usuario no autenticado (sin token)
     * intenta acceder al endpoint y se deniega el acceso con 401.
     */
    @Test
    void testListarUsuarios_retorna401UnauthorizedCuandoNoHayAutenticacion() throws Exception {
        // Ejecución y verificación: se espera un código de estado 401 Unauthorized
        mockMvc.perform(get("/usuario")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Prueba el caso de borde: el servicio retorna una lista vacía de usuarios.
     * Se espera un 200 OK con una página de contenido vacío.
     */
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testListarUsuarios_retorna200OKConPaginaVacia() throws Exception {
        // 1. Comportamiento del "mock": retorna una página vacía
        when(usuarioService.listar(any())).thenReturn(Page.empty());

        // 2. Ejecución y verificación de la prueba
        mockMvc.perform(get("/usuario")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.empty").value(true));
    }


    // --- NUEVOS TESTS PARA ACTUALIZAR Y ELIMINAR USUARIOS ---

    /**
     * Prueba: un ADMINISTRADOR puede actualizar el perfil de cualquier usuario.
     */
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testActualizarUsuario_exitoComoAdministrador() throws Exception {
        Long idUsuarioAActualizar = 2L;
        // Se corrige la llamada al constructor para incluir una lista vacía de permisos
        DatosActualizarUsuarioDTO datosActualizacion = new DatosActualizarUsuarioDTO(
                "Nuevo Nombre", "nuevo.correo@ejemplo.com", List.of());

        Usuario usuarioActualizado = new Usuario("Nuevo Nombre", "nuevo.correo@ejemplo.com", "pass", List.of());
        usuarioActualizado.setId(idUsuarioAActualizar);

        when(usuarioService.actualizar(any(Long.class), any(DatosActualizarUsuarioDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuario/{id}", idUsuarioAActualizar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Nuevo Nombre\", \"correoElectronico\":\"nuevo.correo@ejemplo.com\"}"))
                // Aquí se añade el método `andDo(print())` para imprimir
                // la información completa de la petición y la respuesta en la consola.
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idUsuarioAActualizar))
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"));
    }


    /**
     * Prueba: un usuario puede actualizar su propio perfil.
     * Usamos la anotación @WithMockCustomUser que ya tienes en otro archivo.
     */
    @Test
    @WithMockCustomUser(id = 1L)
    void testActualizarUsuario_exitoComoUsuarioDueño() throws Exception {
        Long idUsuarioAutenticado = 1L;
        // Se corrige la llamada al constructor para incluir una lista vacía de permisos
        DatosActualizarUsuarioDTO datosActualizacion = new DatosActualizarUsuarioDTO(
                "Nuevo Nombre", "nuevo.correo@ejemplo.com", List.of());

        Usuario usuarioActualizado = new Usuario("Nuevo Nombre", "nuevo.correo@ejemplo.com", "pass", List.of());
        usuarioActualizado.setId(idUsuarioAutenticado);

        when(usuarioService.actualizar(any(Long.class), any(DatosActualizarUsuarioDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/usuario/{id}", idUsuarioAutenticado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Nuevo Nombre\", \"correoElectronico\":\"nuevo.correo@ejemplo.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idUsuarioAutenticado))
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"));
    }

    /**
     * Prueba: un usuario intenta actualizar el perfil de otro usuario y es denegado.
     */
    @Test
    @WithMockCustomUser(id = 1L)
    void testActualizarUsuario_denegadoParaUsuarioNoDueño() throws Exception {
        Long idUsuarioAutenticado = 1L;
        Long idUsuarioAActualizar = 2L;

        mockMvc.perform(put("/usuario/{id}", idUsuarioAActualizar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Nombre Falso\", \"correoElectronico\":\"falso@ejemplo.com\"}"))
                .andExpect(status().isForbidden());
    }

    /**
     * Prueba: un ADMINISTRADOR puede eliminar cualquier usuario.
     */
    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void testEliminarUsuario_exitoComoAdministrador() throws Exception {
        Long idUsuarioAEliminar = 1L;
        doNothing().when(usuarioService).eliminar(idUsuarioAEliminar);

        mockMvc.perform(delete("/usuario/{id}", idUsuarioAEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /**
     * Prueba: un usuario puede eliminar su propia cuenta.
     */
  /*  @Test
    @WithMockCustomUser(id = 1L)
    void testEliminarUsuario_exitoComoUsuarioDueño() throws Exception {
        Long idUsuarioAutenticado = 1L;
        doNothing().when(usuarioService).eliminar(idUsuarioAutenticado);

        mockMvc.perform(delete("/usuario/{id}", idUsuarioAutenticado)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }*/

    /**
     * Prueba: un usuario intenta eliminar la cuenta de otro usuario y es denegado.
     */
    @Test
    @WithMockCustomUser(id = 1L)
    void testEliminarUsuario_denegadoParaUsuarioNoDueño() throws Exception {
        Long idUsuarioAutenticado = 1L;
        Long idUsuarioAEliminar = 2L;

        mockMvc.perform(delete("/usuario/{id}", idUsuarioAEliminar)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
