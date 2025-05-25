package pl.edu.streamfinder.show;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("film")
public class Film extends Show {
    private String streamingOptionsId;
    private int releaseYear;
}
