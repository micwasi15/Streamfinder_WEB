package pl.edu.streamfinder.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.edu.streamfinder.show.Show;

import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private long id;
    private String username;
    private String passwordHash;
    private String email;
    private List<Show> favoriteShows;
}
