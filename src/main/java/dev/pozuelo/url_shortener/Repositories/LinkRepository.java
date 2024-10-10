package dev.pozuelo.url_shortener.Repositories;

import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import dev.pozuelo.url_shortener.Entities.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends MongoRepository<Link, String> {
    List<Link> findByUser(String username);
}
