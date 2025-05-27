package pl.edu.streamfinder.streamignOption;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "streamingOptions")
public class StreamingOptions {
    @Id
    private String id;
    private List<StreamingOptionsByCountry> streamingOptions;
}
