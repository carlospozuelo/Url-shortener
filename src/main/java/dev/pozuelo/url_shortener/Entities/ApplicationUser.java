package dev.pozuelo.url_shortener.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

import static dev.pozuelo.url_shortener.Config.SecurityConfig.USER_ROLE;

@Data
@Document("User")
public class ApplicationUser {
    @Id
    private String id;
    @JsonIgnore
    private String password;

    @Indexed(unique = true)
    private String username;

    private List<String> roles;

    private Set<String> links;

    public ApplicationUser(String username, String encodedPassword) {
        this();
        this.username = username;
        this.password = encodedPassword;
    }

    public ApplicationUser() {
        roles = new ArrayList<>();
        links = new HashSet<>();
        roles.add(USER_ROLE);
    }
}
