package com.aluracurso.foro_hub_auth_service.presentation.controller;

import com.aluracurso.foro_hub_auth_service.aplicacion.service.UsuarioService;
import com.aluracurso.foro_hub_auth_service.dominio.perfil.Perfil;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.infraestructura.config.TokenService;
import com.aluracurso.foro_hub_auth_service.infraestructura.config.TestSecurityConfiguration; // Importa la clase de configuración de prueba
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import; // Importa la anotación @Import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                // Ahora, con la configuración de seguridad importada, esta prueba pasará.
                .andExpect(status().isForbidden());
    }

    /**
     * Prueba el caso de fallo: un usuario no autenticado (sin token)
     * intenta acceder al endpoint y se deniega el acceso con 401.
     */
    @Test
    void testListarUsuarios_retorna401UnauthorizedCuandoNoHayAutenticacion() throws Exception {
        // Ejecución y verificación: se espera un código de estado 401 Unauthorized
        // No se usa @WithMockUser aquí para simular un usuario no autenticado.
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
}
