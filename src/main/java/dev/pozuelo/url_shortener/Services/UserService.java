package dev.pozuelo.url_shortener.Services;

import dev.pozuelo.url_shortener.DTOs.UserDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Exceptions.URLShortenerException;
import dev.pozuelo.url_shortener.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    public ApplicationUser createUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new URLShortenerException("A user with that ID already exists", HttpStatus.CONFLICT);
        }

        ApplicationUser user = new ApplicationUser(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
        return user;
    }
}
