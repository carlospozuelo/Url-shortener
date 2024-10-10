package dev.pozuelo.url_shortener.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LinkDTO {
    @NotNull(message = "linkAlias can't be null")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$",
            message = "linkAlias can only contain alphanumeric characters, '-' and '_'")
    private String linkAlias;

    @NotNull(message = "link can't be null")
    @Pattern(regexp = "(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.\\S{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})",
    message = "Link has to ba valid")
    private String link;
}
