package pl.edu.streamfinder.streamingPlatform;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StreamingPlatformRepository extends MongoRepository<StreamingPlatform, String> {
    StreamingPlatform findByName(String name);
}
