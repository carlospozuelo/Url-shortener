package dev.pozuelo.url_shortener.Entities;

import lombok.Data;

@Data
public class LinkStats {

    private long visits;

    public void addVisit() {
        visits++;
    }
}
