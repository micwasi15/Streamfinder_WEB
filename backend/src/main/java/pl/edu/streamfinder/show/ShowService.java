package pl.edu.streamfinder.show;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MongoTemplate mongoTemplate;

    public ShowService(ShowRepository showRepository, MongoTemplate mongoTemplate) {
        this.showRepository = showRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<?> debugTypes() {
        return showRepository.findAll().stream()
                .map(Object::getClass)
                .toList();
    }

    public Page<Show> searchShows(ShowSearchCriteria criteria) {
        List<Criteria> filters = new ArrayList<>();

        if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
            filters.add(Criteria.where("title")
                    .regex(".*" + Pattern.quote(criteria.getTitle()) + ".*", "i"));
        }

        if (criteria.getGenres() != null && !criteria.getGenres().isEmpty()) {
            filters.add(Criteria.where("genres").in(criteria.getGenres()));
        }

        if (criteria.getActors() != null && !criteria.getActors().isEmpty()) {
            filters.add(Criteria.where("actors").in(criteria.getActors()));
        }

        if (criteria.getDirectors() != null && !criteria.getDirectors().isEmpty()) {
            filters.add(Criteria.where("directors").in(criteria.getDirectors()));
        }

        if (criteria.getCountries() != null && !criteria.getCountries().isEmpty()) {
            filters.add(Criteria.where("countryNames").in(criteria.getCountries()));
        }

        if (criteria.getPlatforms() != null && !criteria.getPlatforms().isEmpty()) {
            filters.add(Criteria.where("streamingPlatforms").in(criteria.getPlatforms()));
        }

        Query query = new Query();

        if (!filters.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));
        }

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getSortBy()));
        query.with(pageable);

        long total = mongoTemplate.count(query, Show.class);
        List<Show> content = mongoTemplate.find(query, Show.class);

        return new PageImpl<>(content, pageable, total);
    }
}
