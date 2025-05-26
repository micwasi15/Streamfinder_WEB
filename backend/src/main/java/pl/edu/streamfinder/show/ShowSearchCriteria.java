package pl.edu.streamfinder.show;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowSearchCriteria {
    private String title;
    private List<String> actors;
    private List<String> directors;
    private List<String> genres;
    private List<String> countries;
    private List<String> platforms;
    private int minRating;
    private int minYear;
    private int maxYear;
    private int page = 0;
    private int size = 10;
    private String sortBy = "title";
}
