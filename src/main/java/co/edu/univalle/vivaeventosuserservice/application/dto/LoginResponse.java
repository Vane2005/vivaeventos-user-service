package co.edu.univalle.vivaeventosuserservice.application.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
    }
}
