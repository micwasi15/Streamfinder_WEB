package pl.edu.streamfinder.show;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import pl.edu.streamfinder.streamignOption.StreamingOptions;

@Data
public class Episode {
    private String streamingOptionsId;
    @Transient
    private StreamingOptions streamingOptions;
    private String title;
    private int airYear;
    private String overview;
}
