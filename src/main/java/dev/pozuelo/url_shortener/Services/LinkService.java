package dev.pozuelo.url_shortener.Services;

import dev.pozuelo.url_shortener.DTOs.LinkDTO;
import dev.pozuelo.url_shortener.DTOs.PatchLinkDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Entities.Link;
import dev.pozuelo.url_shortener.Exceptions.URLShortenerException;
import dev.pozuelo.url_shortener.Repositories.LinkRepository;
import dev.pozuelo.url_shortener.Repositories.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final ApplicationUserDetailsService userDetailsService;


    @Autowired
    public LinkService(LinkRepository linkRepository,
                       UserRepository userRepository,
                       ApplicationUserDetailsService userDetailsService) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    public Link useLink(String alias) {
        Optional<Link> optionalLink = linkRepository.findById(alias);
        if (optionalLink.isEmpty()) {
            throw URLShortenerException.linkNotFound(alias);
        }

        Link link = optionalLink.get();

        if (!link.isActive()) {
            throw new URLShortenerException("This link is disabled", HttpStatus.FORBIDDEN);
        }

        // Add a visit to the statistics.
        link.getStatistics().addVisit();
        // Save link again
        linkRepository.save(link);

        return link;
    }

    public Link createLink(@NotNull LinkDTO linkDTO) {
        if (linkDTO == null) { return null; }
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

            Link link = new Link(linkDTO.getLinkAlias(), linkDTO.getLink(), username);
            // Save the link in the link repository.
            linkRepository.save(link);

            return link;
        }
        return null;
    }

    public Link patchLink(@NotNull PatchLinkDTO dto, String linkAlias) {
        if (dto == null) { return null; }
        UserDetails currentUser = userDetailsService.getCurrentUser();
        if (currentUser != null) {
            Optional<Link> optional = linkRepository.findById(linkAlias);
            if (optional.isEmpty()) {
                throw URLShortenerException.linkNotFound(linkAlias);
            }

            Link link = optional.get();

            if (link.getUser() != null && link.getUser().equals(currentUser.getUsername())) {
                link.setActive(dto.isActive());

                linkRepository.save(link);
                return link;
            } else {
                throw new URLShortenerException("The link belongs to another user", HttpStatus.FORBIDDEN);
            }
        }

        return null;
    }

    public List<Link> getUserLinks() {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        if (currentUser != null) {
            return linkRepository.findByUser(currentUser.getUsername());
        }
        return null;
    }


}
