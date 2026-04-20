import java.util.Optional;
import co.edu.univalle.vivaeventosuserservice.domain.model.User;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
