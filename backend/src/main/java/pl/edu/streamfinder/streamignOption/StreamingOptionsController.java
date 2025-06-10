package pl.edu.streamfinder.streamignOption;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamingOptionsController {
    private final StreamingOptionsService streamingOptionsService;

    public StreamingOptionsController(StreamingOptionsService streamingOptionsService) {
        this.streamingOptionsService = streamingOptionsService;
    }

    @GetMapping("/api/public/streaming-options/{id}")
    public ResponseEntity<StreamingOptions> getStreamingOptions(@PathVariable String id) {
        StreamingOptions streamingOptions = streamingOptionsService.getStreamingOptions(id);
        return ResponseEntity.ok(streamingOptions);
    }
}
