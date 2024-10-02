package dev.pozuelo.url_shortener.Repositories;

import dev.pozuelo.url_shortener.Entities.ApplicationUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<ApplicationUser, String> {

    Optional<ApplicationUser> findByUsername(String username);
}
