package co.edu.univalle.vivaeventosuserservice.infrastructure.web;

import co.edu.univalle.vivaeventosuserservice.application.dto.LoginRequest;
import co.edu.univalle.vivaeventosuserservice.application.dto.LoginResponse;
import co.edu.univalle.vivaeventosuserservice.application.dto.RegisterUserDTO;
import co.edu.univalle.vivaeventosuserservice.application.usecase.LoginUser;
import co.edu.univalle.vivaeventosuserservice.application.usecase.RegisterUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, UserController.class})
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginUser loginUser;

    @MockitoBean
    private RegisterUser registerUser;

    // ── POST /auth/login ──────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/login debe retornar 200 con token cuando credenciales son válidas")
    void shouldReturn200WithTokenWhenCredentialsAreValid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("password123");

        LoginResponse response = new LoginResponse("jwt-token-123", 1L);
        when(loginUser.execute(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("POST /auth/login debe llamar al use case con las credenciales correctas")
    void shouldCallLoginUseCaseWithCorrectCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("password123");

        when(loginUser.execute(any(LoginRequest.class)))
                .thenReturn(new LoginResponse("token", 1L));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(loginUser).execute(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login debe retornar 401 cuando credenciales son inválidas")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("wrongpassword");

        when(loginUser.execute(any(LoginRequest.class)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── POST /auth/register ───────────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/register debe retornar 200 cuando el registro es exitoso")
    void shouldReturn200WhenRegistrationIsSuccessful() throws Exception {
        RegisterUserDTO request = new RegisterUserDTO();
        request.setName("Juan Pérez");
        request.setEmail("juan@test.com");
        request.setPassword("password123");

        doNothing().when(registerUser).execute(any(RegisterUserDTO.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("El usuario se ha registrado correctamente"));
    }

    @Test
    @DisplayName("POST /auth/register debe llamar al use case con los datos correctos")
    void shouldCallRegisterUseCaseWithCorrectData() throws Exception {
        RegisterUserDTO request = new RegisterUserDTO();
        request.setName("Juan Pérez");
        request.setEmail("juan@test.com");
        request.setPassword("password123");

        doNothing().when(registerUser).execute(any(RegisterUserDTO.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(registerUser).execute(any(RegisterUserDTO.class));
    }

    @Test
    @DisplayName("POST /auth/register debe retornar 409 cuando el email ya existe")
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        RegisterUserDTO request = new RegisterUserDTO();
        request.setName("Juan Pérez");
        request.setEmail("existing@test.com");
        request.setPassword("password123");

        doThrow(new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.CONFLICT, "Email ya registrado"))
                .when(registerUser).execute(any(RegisterUserDTO.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}