package dev.pozuelo.url_shortener.Repositories;

import dev.pozuelo.url_shortener.Entities.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkRepository extends MongoRepository<Link, String> {
}
