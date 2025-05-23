package pl.edu.streamfinder.show;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "shows")
public class Show {
    @Id
    private int id;
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
}
