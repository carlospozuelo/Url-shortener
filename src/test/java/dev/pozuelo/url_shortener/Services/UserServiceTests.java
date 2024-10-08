package dev.pozuelo.url_shortener.Services;

import dev.pozuelo.url_shortener.DTOs.UserDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Exceptions.URLShortenerException;
import dev.pozuelo.url_shortener.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        ApplicationUser user1 = new ApplicationUser("user1", "password1");
        ApplicationUser user2 = new ApplicationUser("user2", "password2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<ApplicationUser> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser_Success() {
        UserDTO userDTO = new UserDTO("password", "user");
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        ApplicationUser user = new ApplicationUser("newuser", "encodedPassword");
        when(userRepository.save(any(ApplicationUser.class))).thenReturn(user);

        // Act
        ApplicationUser result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, times(1)).save(any(ApplicationUser.class));
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        UserDTO userDTO = new UserDTO("password", "user");
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(new ApplicationUser()));

        URLShortenerException exception = assertThrows(URLShortenerException.class, () -> userService.createUser(userDTO));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(userRepository, times(1)).findByUsername("user");
        verify(userRepository, never()).save(any(ApplicationUser.class));
    }
}
