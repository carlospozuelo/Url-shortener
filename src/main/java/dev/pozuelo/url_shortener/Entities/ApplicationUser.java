package dev.pozuelo.url_shortener.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("User")
public class ApplicationUser {
    @Id
    private String id;
    @JsonIgnore
    private String password;

    @Indexed(unique = true)
    private String username;

    public ApplicationUser(String username, String encodedPassword) {
        this.username = username;
        this.password = encodedPassword;
    }

    public ApplicationUser() {}
}
