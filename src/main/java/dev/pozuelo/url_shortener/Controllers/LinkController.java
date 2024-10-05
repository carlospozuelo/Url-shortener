package dev.pozuelo.url_shortener.Controllers;

import dev.pozuelo.url_shortener.DTOs.LinkDTO;
import dev.pozuelo.url_shortener.Entities.Link;
import dev.pozuelo.url_shortener.Services.LinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.pozuelo.url_shortener.Config.SecurityConfig.HAS_USER_ROLE;

@RestController
@RequestMapping("links")
public class LinkController {

    private LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    /**
     * POST /create : Create a link
     * Creates a link and associates it to the current user
     *
     * @param linkDTO The user basic information, including username and password
     * @return OK (status code 200)
     */
    @PostMapping("/create")
    @PreAuthorize(HAS_USER_ROLE)
    public ResponseEntity<Link> createLink(@Valid @NotNull @RequestBody LinkDTO linkDTO) {
        Link link = linkService.createLink(linkDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(link);
    }
}
