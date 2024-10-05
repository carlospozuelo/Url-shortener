package dev.pozuelo.url_shortener.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LinkDTO {
    @NotNull(message = "linkAlias can't be null")
    private String linkAlias;
    @NotNull(message = "link can't be null")
    private String link;
}
