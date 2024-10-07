package dev.pozuelo.url_shortener.Controllers;

import dev.pozuelo.url_shortener.Entities.Link;
import dev.pozuelo.url_shortener.Services.LinkService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("r")
public class RedirectionController {

    private final LinkService linkService;

    @Autowired
    public RedirectionController(LinkService linkService) {
        this.linkService = linkService;
    }

    /**
     * GET /r/{linkAlias} : Redirects to a link
     * Redirects to the link that is stored in the database with that alias.
     *
     * @param linkAlias The alias of the link stored in the database
     * @return OK (status code 200)
     */
    @GetMapping("/{linkAlias}")
    private RedirectView getRedirection(@Parameter(name = "linkAlias", description = "The alias of the requested link", required = true, in = ParameterIn.PATH)
                                                @PathVariable("linkAlias") String linkAlias) {
        Link link = linkService.useLink(linkAlias);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link.getLink());
        return redirectView;
    }

    /**
     * GET /r/{linkAlias} : Gets a link information
     * Returns the link information of the link passed by paraemter.
     * This endpoint counts as a link visit, so it affects statistics.
     *
     * @param linkAlias The alias of the link stored in the database
     * @return OK (status code 200)
     */
    @GetMapping("/full/{linkAlias}")
    private ResponseEntity<Link> getLinkByAlias(
            @Parameter(
                    name = "linkAlias",
                    description = "The alias of the requested link",
                    required = true, in = ParameterIn.PATH)
            @PathVariable("linkAlias") String linkAlias) {
        Link link = linkService.useLink(linkAlias);

        return ResponseEntity.ok(link);
    }
}
