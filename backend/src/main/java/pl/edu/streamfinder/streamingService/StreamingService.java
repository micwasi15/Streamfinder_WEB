package pl.edu.streamfinder.streamingService;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collation = "services")
public class StreamingService {
    @Id
    private String id;
    private String name;
    private String logoURL;
    List<Plan> plans;
}
