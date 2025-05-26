package pl.edu.streamfinder.show;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "showType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Film.class, name = "film"),
    @JsonSubTypes.Type(value = Series.class, name = "series")
})
@TypeAlias("show")
@Data
@Document(collection = "shows")
public class Show {
    @Id
    private String id;
    private int apiId;
    private String title;
    private String imdbId;
    private String tmdbId;
    private int rating;
    private List<String> cast;
    private List<String> directors;
    private List<String> genres;
    private String overview;
    private String showType;
    private String trailerURL;
    private ImageSet imageSet;
    private List<String> countryNames;
    private List<String> streamingPlatforms;
}
