package dev.pozuelo.url_shortener.Controllers;

import dev.pozuelo.url_shortener.DTOs.LinkDTO;
import dev.pozuelo.url_shortener.DTOs.PatchLinkDTO;
import dev.pozuelo.url_shortener.Entities.Link;
import dev.pozuelo.url_shortener.Services.LinkService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("links")
public class LinkController {

    private LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    /**
     * POST /links/create : Create a link
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
    /**
     * Get /links : Gets all links
     * Gets all the links from the current user
     *
     * @return OK (status code 200)
     */
    @GetMapping("/")
    @PreAuthorize(HAS_USER_ROLE)
    public ResponseEntity<List<Link>> getLinks() {
        List<Link> links = linkService.getUserLinks();
        return ResponseEntity.ok(links);
    }

    /**
     * PATCH /links/patch/{linkAlias} : Patches a link
     * Changes fields in a link. This method is used to activate / deactivate the link.
     */
    @PatchMapping("/patch/{linkAlias}")
    @PreAuthorize(HAS_USER_ROLE)
    public ResponseEntity<Link> patchLink(@RequestBody @Valid PatchLinkDTO body,
                                          @Parameter(
                                                  name = "linkAlias",
                                                  description = "The alias of the requested link",
                                                  required = true, in = ParameterIn.PATH)
                                          @PathVariable String linkAlias) {
        Link link = linkService.patchLink(body, linkAlias);

        return ResponseEntity.ok(link);
    }
}
