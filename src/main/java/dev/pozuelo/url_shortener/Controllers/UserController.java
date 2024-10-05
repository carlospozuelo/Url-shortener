package dev.pozuelo.url_shortener.Controllers;

import dev.pozuelo.url_shortener.DTOs.UserDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.pozuelo.url_shortener.Config.SecurityConfig.HAS_USER_ROLE;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /users : Get all users
     * Returns all users
     */
    @GetMapping("/users")
    @PreAuthorize(HAS_USER_ROLE)
    public ResponseEntity<List<ApplicationUser>> getAll() {
        List<ApplicationUser> users = userService.getAllUsers();

        return ResponseEntity.ok().body(users);
    }

    /**
     * POST /register : Register user
     * Creates a user account
     *
     * @param userDTO The user basic information, including username and password
     * @return OK (status code 200)
     */
    @PostMapping("/register")
    public ResponseEntity<ApplicationUser> register(@Valid @NotNull @RequestBody UserDTO userDTO) {
        ApplicationUser user = userService.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
