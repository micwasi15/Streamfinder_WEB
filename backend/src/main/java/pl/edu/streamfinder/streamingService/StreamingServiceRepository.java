package pl.edu.streamfinder.streamingService;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StreamingServiceRepository extends MongoRepository<StreamingService, String> {
    StreamingService findByName(String name);
}
