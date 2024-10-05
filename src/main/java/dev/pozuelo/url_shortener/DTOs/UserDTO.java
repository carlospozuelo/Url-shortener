package dev.pozuelo.url_shortener.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {
    @NotNull(message = "Password can't be null")
    private String password;
    @NotNull(message = "Username can't be null")
    private String username;
}
