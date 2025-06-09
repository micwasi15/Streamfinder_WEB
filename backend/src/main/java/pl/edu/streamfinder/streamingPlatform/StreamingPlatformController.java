package pl.edu.streamfinder.streamingPlatform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamingPlatformController {
    private final StreamingPlatformService streamingPlatformService;

    public StreamingPlatformController(StreamingPlatformService streamingPlatformService) {
        this.streamingPlatformService = streamingPlatformService;
    }

    @GetMapping("api/public/streaming-platforms/{name}")
    public StreamingPlatform getStreamingPlatformByName(@PathVariable String name) {
        return streamingPlatformService.getStreamingPlatformByName(name);
    }
}
