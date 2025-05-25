package pl.edu.streamfinder.show;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
@Data
@Document(collection = "shows")
public class Show {
    @Id
    private String id;
    private String title;
    private String imdbId;
    private String tmdbId;
    private int rating;
    private List<String> cast;
    private List<String> directors;
    private List<String> genres;
    private String overwiew;
    private String showType;
    private String trailerURL;
}
