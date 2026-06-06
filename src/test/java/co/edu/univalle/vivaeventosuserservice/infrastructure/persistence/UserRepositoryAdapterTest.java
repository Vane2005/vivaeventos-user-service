package co.edu.univalle.vivaeventosuserservice.infrastructure.persistence;

import co.edu.univalle.vivaeventosuserservice.domain.model.Role;
import co.edu.univalle.vivaeventosuserservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository jpaRepository;

    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UserRepositoryAdapter(jpaRepository);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setName("John");
        entity.setEmail("john@example.com");
        entity.setPassword("hashed");
        entity.setRole(Role.CLIENTE);

        when(jpaRepository.findByEmail("john@example.com")).thenReturn(Optional.of(entity));

        Optional<User> result = adapter.findByEmail("john@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
        assertThat(result.get().getRole()).isEqualTo(Role.CLIENTE);
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotExists() {
        when(jpaRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> result = adapter.findByEmail("notfound@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldReturnSavedUser_WithCorrectRole() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("hashed");
        user.setRole(Role.GERENTE);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);
        savedEntity.setName("John");
        savedEntity.setEmail("john@example.com");
        savedEntity.setPassword("hashed");
        savedEntity.setRole(Role.GERENTE);

        when(jpaRepository.save(any())).thenReturn(savedEntity);

        User result = adapter.save(user);

        assertThat(result.getRole()).isEqualTo(Role.GERENTE);
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void save_ShouldDefaultToCliente_WhenRoleIsNull() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("hashed");
        user.setRole(null);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);
        savedEntity.setName("John");
        savedEntity.setEmail("john@example.com");
        savedEntity.setPassword("hashed");
        savedEntity.setRole(Role.CLIENTE);

        when(jpaRepository.save(any())).thenReturn(savedEntity);

        User result = adapter.save(user);

        assertThat(result.getRole()).isEqualTo(Role.CLIENTE);
    }
}