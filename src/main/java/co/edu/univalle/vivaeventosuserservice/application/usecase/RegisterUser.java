package co.edu.univalle.vivaeventosuserservice.application.usecase;

import co.edu.univalle.vivaeventosuserservice.domain.model.User;
import co.edu.univalle.vivaeventosuserservice.application.dto.RegisterUserDTO;
import co.edu.univalle.vivaeventosuserservice.infrastructure.persistence.UserEntity;
import co.edu.univalle.vivaeventosuserservice.infrastructure.persistence.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUser {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUser(UserJpaRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(RegisterUserDTO request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Este correo ya está registrado");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword()); // encriptamos la clave

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());

        userRepository.save(userEntity);
    }
}
