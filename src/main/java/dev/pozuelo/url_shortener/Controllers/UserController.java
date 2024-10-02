package dev.pozuelo.url_shortener.Controllers;

import dev.pozuelo.url_shortener.DTOs.UserDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationUser> register(@Valid @NotNull @RequestBody UserDTO userDTO) {
        ApplicationUser user = userService.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/boo")
    public String boo() {
        return "Ahhh!!";
    }

}
