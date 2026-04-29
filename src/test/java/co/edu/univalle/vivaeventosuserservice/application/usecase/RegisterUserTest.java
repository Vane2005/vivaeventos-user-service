package co.edu.univalle.vivaeventosuserservice.application.usecase;

import co.edu.univalle.vivaeventosuserservice.application.dto.RegisterUserDTO;
import co.edu.univalle.vivaeventosuserservice.domain.model.User;
import co.edu.univalle.vivaeventosuserservice.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterUserTest {

    private RegisterUser registerUser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerUser = new RegisterUser(userRepository, passwordEncoder);
    }

    @Test
    void execute_ShouldRegisterUser_WhenEmailIsUnique() {
        // Arrange
        RegisterUserDTO request = new RegisterUserDTO();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");

        // Act
        registerUser.execute(request);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void execute_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        RegisterUserDTO request = new RegisterUserDTO();
        request.setEmail("existing@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> registerUser.execute(request));
        assertEquals("Este correo ya está registrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
