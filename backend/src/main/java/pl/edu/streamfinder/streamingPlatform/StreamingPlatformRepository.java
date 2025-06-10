package pl.edu.streamfinder.streamingPlatform;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StreamingPlatformRepository extends MongoRepository<StreamingPlatform, String> {
    Optional<StreamingPlatform> findByName(String name);
}
