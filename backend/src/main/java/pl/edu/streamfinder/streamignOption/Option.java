package pl.edu.streamfinder.streamignOption;

import lombok.Data;
import pl.edu.streamfinder.streamingPlatform.StreamingPlatform;

@Data
public class Option {
    private String link;
    private String videoLink;
    private StreamingPlatform service;
}
