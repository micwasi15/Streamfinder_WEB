package pl.edu.streamfinder.show;

import lombok.Data;

@Data
public class Episode {
    private String streamingOptionsId;
    private String title;
    private int airYear;
    private String overview;
}
