package pl.edu.streamfinder.streamignOption;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class StreamingOptionsService {
    private final StreamingOptionsRepository streamingOptionsRepository;

    public StreamingOptionsService(StreamingOptionsRepository streamingOptionsRepository) {
        this.streamingOptionsRepository = streamingOptionsRepository;
    }

    public StreamingOptions getStreamingOptions(String id) {
        return streamingOptionsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Streaming options not found for ID: " + id));
    }
}
