package dev.pozuelo.url_shortener.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotNull(message = "Password can't be null")
    private String password;
    @NotNull(message = "Username can't be null")
    private String username;
}
