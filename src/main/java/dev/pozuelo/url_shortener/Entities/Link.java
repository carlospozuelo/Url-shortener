package dev.pozuelo.url_shortener.Entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Link")
public class Link {

    @Id
    private String linkAlias;
    private String link;
    private boolean isActive = true;

    private LinkStats statistics;

    public Link() {
        this.statistics = new LinkStats();
    }

    public Link(String linkAlias, String link) {
        this.linkAlias = linkAlias;
        this.link = link;
    }
}
