package dev.pozuelo.url_shortener.Services;

import dev.pozuelo.url_shortener.DTOs.LinkDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Entities.Link;
import dev.pozuelo.url_shortener.Exceptions.URLShortenerException;
import dev.pozuelo.url_shortener.Repositories.LinkRepository;
import dev.pozuelo.url_shortener.Repositories.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LinkService {

    private LinkRepository linkRepository;
    private UserRepository userRepository;
    private ApplicationUserDetailsService userDetailsService;


    @Autowired
    public LinkService(LinkRepository linkRepository,
                       UserRepository userRepository,
                       ApplicationUserDetailsService userDetailsService) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    // @Transactional
    public Link createLink(@NotNull LinkDTO linkDTO) {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        if (currentUser != null) {
            // Check that no link is already present with that id
            if (linkRepository.existsById(linkDTO.getLinkAlias())) {
                throw new URLShortenerException("A link with that alias is already registered in the system.",
                        HttpStatus.CONFLICT);
            }

            // Get user from the currently authenticated user.
            String username = currentUser.getUsername();
            Optional<ApplicationUser> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                throw new URLShortenerException("A optionalUser with username "
                        + username +
                        " was not found in the optionalUser repository, yet that optionalUser is currently authenticated.");
            }
            ApplicationUser user = optionalUser.get();

            Link link = new Link(linkDTO.getLinkAlias(), linkDTO.getLink());
            // Save the link in the link repository.
            linkRepository.save(link);
            // Update the optionalUser with the new link in the optionalUser repository.
            user.getLinks().add(linkDTO.getLinkAlias());
            userRepository.save(user);

            return link;
        }
        return null;
    }


}
