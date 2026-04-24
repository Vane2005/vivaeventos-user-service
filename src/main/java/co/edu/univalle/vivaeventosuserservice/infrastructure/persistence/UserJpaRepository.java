package co.edu.univalle.vivaeventosuserservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.univalle.vivaeventosuserservice.infrastructure.persistence.UserEntity;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
