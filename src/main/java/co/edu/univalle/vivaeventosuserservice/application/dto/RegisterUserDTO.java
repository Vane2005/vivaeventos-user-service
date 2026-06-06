package co.edu.univalle.vivaeventosuserservice.application.dto;

import co.edu.univalle.vivaeventosuserservice.domain.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
