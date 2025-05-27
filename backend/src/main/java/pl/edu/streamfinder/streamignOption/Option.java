package pl.edu.streamfinder.streamignOption;

import lombok.Data;
import pl.edu.streamfinder.streamingService.StreamingService;

@Data
public class Option {
    private String link;
    private String videoLink;
    private StreamingService service;
}
