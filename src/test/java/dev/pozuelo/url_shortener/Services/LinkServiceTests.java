package dev.pozuelo.url_shortener.Services;

import dev.pozuelo.url_shortener.DTOs.LinkDTO;
import dev.pozuelo.url_shortener.DTOs.PatchLinkDTO;
import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Entities.Link;
import dev.pozuelo.url_shortener.Exceptions.URLShortenerException;
import dev.pozuelo.url_shortener.Repositories.LinkRepository;
import dev.pozuelo.url_shortener.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LinkServiceTests {

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationUserDetailsService userDetailsService;

    @InjectMocks
    private LinkService linkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* Create link tests */
    @Test
    void createLink_Success() {
        LinkDTO linkDTO = correctLinkDTO();
        Link correctLink = correctLink();

        Mockito.when(userDetailsService.getCurrentUser())
                .thenReturn(User.builder().username("testUser").password("1234").build());
        Mockito.when(linkRepository.existsById(linkDTO.getLinkAlias())).thenReturn(false);
        ApplicationUser user = new ApplicationUser("testUser", "encodedPassword");
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        Mockito.when(linkRepository.save(any(Link.class))).thenReturn(correctLink);

        Link response = linkService.createLink(linkDTO);

        assertEquals(response, correctLink);
        verify(linkRepository, times(1)).save(any(Link.class));
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(linkRepository, times(1)).existsById(linkDTO.getLinkAlias());
    }

    @Test
    void createLink_LinkAlreadyExists() {
        LinkDTO linkDTO = correctLinkDTO();
        Link correctLink = correctLink();

        Mockito.when(userDetailsService.getCurrentUser())
                .thenReturn(User.builder().username("testUser").password("1234").build());
        Mockito.when(linkRepository.existsById(linkDTO.getLinkAlias())).thenReturn(true);

        URLShortenerException exception = assertThrows(URLShortenerException.class, () -> linkService.createLink(linkDTO));

        assertEquals(exception.getStatusCode(), HttpStatus.CONFLICT);
        verify(linkRepository, times(0)).save(any(Link.class));
    }

    /* Test patch link */
    @Test
    void patchLink_Success() {
        PatchLinkDTO patchLinkDTO = disableLinkDTO();
        Link link = correctLink();
        Link disabledLink = disabledLink();

        String linkAlias = "gl";

        Mockito.when(userDetailsService.getCurrentUser())
                .thenReturn(User.builder().username("testUser").password("1234").build());
        Mockito.when(linkRepository.findById(linkAlias)).thenReturn(Optional.of(link));
        Mockito.when(linkRepository.save(any(Link.class))).thenReturn(disabledLink);

        Link response = linkService.patchLink(patchLinkDTO, linkAlias);

        assertEquals(response, disabledLink);
        verify(userDetailsService, times(1)).getCurrentUser();
        verify(linkRepository, times(1)).findById(linkAlias);
        verify(linkRepository, times(1)).save(any());
    }

    @Test
    void patchLink_LinkNotFound() {
        PatchLinkDTO patchLinkDTO = disableLinkDTO();

        String linkAlias = "gl";

        Mockito.when(userDetailsService.getCurrentUser())
                .thenReturn(User.builder().username("testUser").password("1234").build());
        Mockito.when(linkRepository.findById(linkAlias)).thenReturn(Optional.empty());

        URLShortenerException exception = assertThrows(URLShortenerException.class, () -> linkService.patchLink(patchLinkDTO, linkAlias));
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        verify(userDetailsService, times(1)).getCurrentUser();
        verify(linkRepository, times(1)).findById(linkAlias);
        verify(linkRepository, times(0)).save(any());
    }

    @Test
    void patchLink_LinkBelongsToAnotherUser() {
        PatchLinkDTO patchLinkDTO = disableLinkDTO();
        Link link = correctLink();

        String linkAlias = "gl";

        Mockito.when(userDetailsService.getCurrentUser())
                .thenReturn(User.builder().username("anotherUser").password("1234").build());
        Mockito.when(linkRepository.findById(linkAlias)).thenReturn(Optional.of(link));

        URLShortenerException exception = assertThrows(URLShortenerException.class, () -> linkService.patchLink(patchLinkDTO, linkAlias));
        assertEquals(exception.getStatusCode(), HttpStatus.FORBIDDEN);
        verify(userDetailsService, times(1)).getCurrentUser();
        verify(linkRepository, times(1)).findById(linkAlias);
        verify(linkRepository, times(0)).save(any());
    }

    /* Test use link */
    private void useLink_Success() {
        String linkAlias = "gl";
        Link link = correctLink();
        Link linkWithVisits = correctLink();
        linkWithVisits.getStatistics().addVisit();

        Mockito.when(linkRepository.findById(linkAlias)).thenReturn(Optional.of(link));
        Mockito.when(linkRepository.save(any())).thenReturn(linkWithVisits);

        Link response = linkService.useLink(linkAlias);

        assertEquals(response, linkWithVisits);
        verify(linkRepository, times(1)).findById(linkAlias);
        verify(linkRepository, times(1)).save(link);
    }

    private void useLink_NotFound() {
        String linkAlias = "aws";

        Mockito.when(linkRepository.findById(linkAlias)).thenReturn(Optional.empty());

        URLShortenerException exception = assertThrows(URLShortenerException.class, () -> linkService.useLink(linkAlias));

        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);
        verify(linkRepository, times(1)).findById(linkAlias);
        verify(linkRepository, times(0)).save(any());
    }

    private void useLink_DisabledLink() {
        String linkAlias = "gl";
        Link link = correctLink();
        link.setActive(false);

        Mockito.when(linkRepository.findById(linkAlias)).thenReturn(Optional.of(link));

        URLShortenerException exception = assertThrows(URLShortenerException.class, () -> linkService.useLink(linkAlias));

        assertEquals(exception.getStatusCode(), HttpStatus.FORBIDDEN);
        verify(linkRepository, times(1)).findById(linkAlias);
        verify(linkRepository, times(0)).save(link);
    }

    /* Other methods */
    private LinkDTO correctLinkDTO() {
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setLink("http://google.com");
        linkDTO.setLinkAlias("gl");

        return linkDTO;
    }

    private Link correctLink() {
        Link link = new Link();
        link.setLink("http://google.com");
        link.setLinkAlias("gl");
        link.setUser("testUser");
        return link;
    }

    private Link disabledLink() {
        Link link = correctLink();
        link.setActive(false);
        return link;
    }

    private PatchLinkDTO disableLinkDTO(){
        PatchLinkDTO dto = new PatchLinkDTO();
        dto.setActive(false);
        return dto;
    }

}
