package pl.edu.streamfinder.streamingPlatform;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "platforms")
public class StreamingPlatform {
    @Id
    private String id;
    private String name;
    private String logoURL;
    List<Plan> plans;
}
