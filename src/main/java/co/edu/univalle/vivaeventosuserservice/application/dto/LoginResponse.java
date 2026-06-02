package co.edu.univalle.vivaeventosuserservice.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String token;
    private Long userId;

    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}