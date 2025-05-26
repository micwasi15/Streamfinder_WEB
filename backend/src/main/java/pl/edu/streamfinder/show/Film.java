package pl.edu.streamfinder.show;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

@EqualsAndHashCode(callSuper = true)
@Data
@TypeAlias("film")
public class Film extends Show {
    private String streamingOptionsId;
    private int releaseYear;
}
