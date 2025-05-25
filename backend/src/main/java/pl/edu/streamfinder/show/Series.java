package pl.edu.streamfinder.show;

import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("series")
public class Series extends Show {
    private String seasonTitle;
    private int firstAirYear;
    private int lastAirYear;
    private List<Season> seasons;
}
