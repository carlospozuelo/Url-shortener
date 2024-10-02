package dev.pozuelo.url_shortener.Services;

import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        ApplicationUser user = optionalUser.get();
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
