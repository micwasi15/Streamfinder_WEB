package pl.edu.streamfinder.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String passwordHash;
    private String email;
    private List<String> roles = new ArrayList<>();
    private List<String> favoriteShowIds = new ArrayList<>();
}
