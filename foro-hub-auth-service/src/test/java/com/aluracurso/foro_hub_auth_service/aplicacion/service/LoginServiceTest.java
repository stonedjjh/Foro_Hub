package com.aluracurso.foro_hub_auth_service.aplicacion.service;

import com.aluracurso.foro_hub_auth_service.dominio.usuario.Usuario;
import com.aluracurso.foro_hub_auth_service.dominio.usuario.UsuarioRepository;
import com.aluracurso.foro_hub_auth_service.infraestructura.security.UserDetailsFromEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("LoginServiceTest - Pruebas Unitarias")
class LoginServiceTest {

    // Se utiliza @Mock para crear un mock de la dependencia (el repositorio).
    @Mock
    private UsuarioRepository usuarioRepository;

    // Se utiliza @InjectMocks para inyectar el mock en la clase que vamos a probar.
    @InjectMocks
    private LoginService loginService;

    // Este método se ejecuta antes de cada prueba para inicializar los mocks.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe devolver UserDetails cuando el usuario existe")
    void loadUserByUsername_existente_devuelveUserDetails() {
        // 1. Preparación de los datos (Arrange)
        String correo = "test@example.com";
        // Creación de un POJO de dominio de Usuario para simular el resultado del repositorio.
        Usuario usuarioMock = new Usuario(1L, "Test User", correo, "hashedpassword");

        // Configuración del comportamiento del mock.
        // Cuando se llame a encontrarPorCorreoElectronico con cualquier String, debe devolver el usuario mock.
        when(usuarioRepository.buscarPorCorreoElectronico(anyString())).thenReturn(Optional.of(usuarioMock));

        // 2. Ejecución de la prueba (Act)
        UserDetails userDetails = loginService.loadUserByUsername(correo);

        // 3. Verificación de los resultados (Assert)
        assertNotNull(userDetails, "UserDetails no debería ser nulo");
        assertInstanceOf(UserDetailsFromEntity.class, userDetails, "El objeto devuelto debe ser de tipo UserDetailsFromEntity");
        assertEquals(correo, userDetails.getUsername(), "El nombre de usuario debe coincidir");
    }

    @Test
    @DisplayName("Debe lanzar una excepción cuando el usuario no existe")
    void loadUserByUsername_noExistente_lanzaExcepcion() {
        // 1. Preparación de los datos (Arrange)
        String correoInexistente = "noexistente@example.com";

        // Configuración del comportamiento del mock para simular que el usuario no se encuentra.
        when(usuarioRepository.buscarPorCorreoElectronico(anyString())).thenReturn(Optional.empty());

        // 2. Ejecución y Verificación de la prueba (Act & Assert)
        // Se espera que la llamada a loadUserByUsername lance una UsernameNotFoundException.
        assertThrows(UsernameNotFoundException.class, () -> loginService.loadUserByUsername(correoInexistente),
                "Se esperaba una UsernameNotFoundException para un usuario no existente");
    }
}

