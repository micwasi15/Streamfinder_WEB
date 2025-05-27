package pl.edu.streamfinder.streamignOption;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamingOptionsController {
    private final StreamingOptionsService streamingOptionsService;

    public StreamingOptionsController(StreamingOptionsService streamingOptionsService) {
        this.streamingOptionsService = streamingOptionsService;
    }

    public ResponseEntity<StreamingOptions> getStreamingOptions(String id) {
        StreamingOptions streamingOptions = streamingOptionsService.getStreamingOptions(id);
        return ResponseEntity.ok(streamingOptions);
    }
}
