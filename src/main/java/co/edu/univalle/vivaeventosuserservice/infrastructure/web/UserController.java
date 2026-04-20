package co.edu.univalle.vivaeventosuserservice.infrastructure.web;
import co.edu.univalle.vivaeventosuserservice.application.usecase.RegisterUser;
import co.edu.univalle.vivaeventosuserservice.application.dto.RegisterUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final RegisterUser registerUser;

    public UserController(RegisterUser registerUser) {
        this.registerUser = registerUser;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO request) {
        registerUser.execute(request);

        return ResponseEntity.ok("El usuario se ha registrado correctamente");
    }
}
