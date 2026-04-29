package co.edu.univalle.vivaeventosuserservice.application.usecase;

import co.edu.univalle.vivaeventosuserservice.application.dto.LoginRequest;
import co.edu.univalle.vivaeventosuserservice.domain.model.User;
import co.edu.univalle.vivaeventosuserservice.domain.port.UserRepository;
import co.edu.univalle.vivaeventosuserservice.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LoginUserTest {

    private LoginUser loginUser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginUser = new LoginUser(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void execute_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("mockedToken");

        // Act
        String token = loginUser.execute(request);

        // Assert
        assertEquals("mockedToken", token);
        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), user.getPassword());
        verify(jwtService, times(1)).generateToken(user.getEmail());
    }

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> loginUser.execute(request));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    void execute_ShouldThrowException_WhenPasswordIsIncorrect() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> loginUser.execute(request));
        assertEquals("Contraseña incorrecta", exception.getMessage());
    }
}
