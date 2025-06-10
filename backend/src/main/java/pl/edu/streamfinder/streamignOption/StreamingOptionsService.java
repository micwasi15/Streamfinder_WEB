package pl.edu.streamfinder.streamignOption;

import org.springframework.stereotype.Service;
import pl.edu.streamfinder.exceptions.ObjectNotFoundException;
import pl.edu.streamfinder.streamingPlatform.StreamingPlatform;
import pl.edu.streamfinder.streamingPlatform.StreamingPlatformRepository;

import java.util.Optional;

@Service
public class StreamingOptionsService {
    private final StreamingOptionsRepository streamingOptionsRepository;
    private final StreamingPlatformRepository streamingPlatformRepository;

    public StreamingOptionsService(StreamingOptionsRepository streamingOptionsRepository, StreamingPlatformRepository streamingPlatformRepository) {
        this.streamingOptionsRepository = streamingOptionsRepository;
        this.streamingPlatformRepository = streamingPlatformRepository;
    }

    public StreamingOptions getStreamingOptions(String id) {
        StreamingOptions streamingOptions = streamingOptionsRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Streaming options not found for ID: " + id));
        for (StreamingOptionsByCountry options : streamingOptions.getStreamingOptions()) {
            for (Option option : options.getOptions()) {
                String platformName = option.getService().getName();
                Optional<StreamingPlatform> streamingPlatform = streamingPlatformRepository.findByName(platformName);
                if (streamingPlatform.isPresent()) {
                    option.getService().setId(streamingPlatform.get().getId());
                    option.getService().setLogoURL(streamingPlatform.get().getLogoURL());
                }
            }
        }
        return streamingOptions;
    }
}
