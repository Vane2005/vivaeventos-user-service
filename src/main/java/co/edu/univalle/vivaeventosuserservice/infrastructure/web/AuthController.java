package co.edu.univalle.vivaeventosuserservice.infrastructure.web;

import co.edu.univalle.vivaeventosuserservice.application.dto.LoginRequest;
import co.edu.univalle.vivaeventosuserservice.application.dto.LoginResponse;
import co.edu.univalle.vivaeventosuserservice.application.usecase.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUser loginUser;

    public AuthController(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = loginUser.execute(request);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
