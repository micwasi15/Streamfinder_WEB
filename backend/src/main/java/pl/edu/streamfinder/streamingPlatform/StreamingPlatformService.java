package pl.edu.streamfinder.streamingPlatform;

import org.springframework.stereotype.Service;
import pl.edu.streamfinder.exceptions.ObjectNotFoundException;

@Service
public class StreamingPlatformService {
    private final StreamingPlatformRepository streamingPlatformRepository;

    public StreamingPlatformService(StreamingPlatformRepository streamingPlatformRepository) {
        this.streamingPlatformRepository = streamingPlatformRepository;
    }

    public StreamingPlatform getStreamingPlatformByName(String name) {
        return streamingPlatformRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Streaming platform not found: " + name));
    }
}
