package pl.edu.streamfinder.streamignOption;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "streamingOptions")
public class StreamingOptions {
    @Id
    private String id;
}
