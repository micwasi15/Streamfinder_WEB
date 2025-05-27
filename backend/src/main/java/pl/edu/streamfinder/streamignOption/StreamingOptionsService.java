package pl.edu.streamfinder.streamignOption;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.edu.streamfinder.streamingService.StreamingService;
import pl.edu.streamfinder.streamingService.StreamingServiceRepository;

@Service
public class StreamingOptionsService {
    private final StreamingOptionsRepository streamingOptionsRepository;
    private final StreamingServiceRepository streamingServiceRepository;

    public StreamingOptionsService(StreamingOptionsRepository streamingOptionsRepository, StreamingServiceRepository streamingServiceRepository) {
        this.streamingOptionsRepository = streamingOptionsRepository;
        this.streamingServiceRepository = streamingServiceRepository;
    }

    public StreamingOptions getStreamingOptions(String id) {
        StreamingOptions streamingOptions = streamingOptionsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Streaming options not found for ID: " + id));
        for (StreamingOptionsByCountry options : streamingOptions.getStreamingOptions()) {
            for (Option option : options.getOptions()) {
                String platformName = option.getService().getName();
                StreamingService streamingService = streamingServiceRepository.findByName(platformName);
                if (streamingService != null) {
                    option.getService().setId(streamingService.getId());
                    option.getService().setLogoURL(streamingService.getLogoURL());
                }
            }
        }
        return streamingOptions;
    }
}
