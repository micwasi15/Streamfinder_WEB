package pl.edu.streamfinder.streamingService;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "services")
public class StreamingService {
    @Id
    private String id;
    private String name;
    private String logoURL;
    List<Plan> plans;
}
