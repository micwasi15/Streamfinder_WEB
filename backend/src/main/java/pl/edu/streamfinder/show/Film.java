package pl.edu.streamfinder.show;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import pl.edu.streamfinder.streamignOption.StreamingOptions;

@EqualsAndHashCode(callSuper = true)
@Data
@TypeAlias("film")
public class Film extends Show {
    private String streamingOptionsId;
    @Transient
    private StreamingOptions streamingOptions;
    private int releaseYear;
}
