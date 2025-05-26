package pl.edu.streamfinder.show;

import lombok.Data;

import java.util.List;

@Data
public class Season {
    private String title;
    private int firstAirYear;
    private List<Episode> episodes;
}
