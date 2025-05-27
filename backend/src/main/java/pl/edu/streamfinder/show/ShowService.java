package pl.edu.streamfinder.show;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.edu.streamfinder.streamignOption.StreamingOptionsService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ShowService {
    private final ShowRepository showRepository;
    private final MongoTemplate mongoTemplate;
    private final StreamingOptionsService streamingOptionsService;

    public ShowService(ShowRepository showRepository, MongoTemplate mongoTemplate, StreamingOptionsService streamingOptionsService) {
        this.showRepository = showRepository;
        this.mongoTemplate = mongoTemplate;
        this.streamingOptionsService = streamingOptionsService;
    }

    public List<?> debugTypes() {
        return showRepository.findAll().stream()
                .map(Object::getClass)
                .toList();
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public Page<Show> searchShows(ShowSearchCriteria criteria) {
        return searchShows(criteria, null);
    }

    public Page<Show> searchShows(ShowSearchCriteria criteria, List<String> showIds) {
        List<Criteria> filters = new ArrayList<>();

        if (showIds != null && !showIds.isEmpty()) {
            filters.add(Criteria.where("id").in(showIds));
        }

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

    public Film getFilmById(String id) {
        Film film = (Film) showRepository.findById(id)
                .filter(show -> show instanceof Film).orElse(null);
        if (film != null && film.getStreamingOptionsId() != null) {
            film.setStreamingOptions(
                    streamingOptionsService.getStreamingOptions(film.getStreamingOptionsId()));
        }
        return film;
    }

    public Series getSeriesById(String id) {
        Series series = (Series) showRepository.findById(id)
                .filter(show -> show instanceof Series).orElse(null);
        if (series != null) {
            for (Season season : series.getSeasons()) {
                for (Episode episode : season.getEpisodes()) {
                    if (episode.getStreamingOptionsId() != null) {
                        episode.setStreamingOptions(
                                streamingOptionsService.getStreamingOptions(episode.getStreamingOptionsId()));
                    }
                }
            }
        }
        return series;
    }

    public List<String> getAllStreamingPlatforms() {
        List<Show> shows = showRepository.findAll();
        Set<String> platforms = new HashSet<>();
        for (Show show : shows) {
            if (show.getStreamingPlatforms() != null) {
                platforms.addAll(show.getStreamingPlatforms());
            }
        }
        return new ArrayList<>(platforms);
    }

    public List<String> getAllGenres() {
        List<Show> shows = showRepository.findAll();
        Set<String> genres = new HashSet<>();
        for (Show show : shows) {
            if (show.getGenres() != null) {
                genres.addAll(show.getGenres());
            }
        }
        return new ArrayList<>(genres);
    }

    public List<PlatformStats> getPlatformStats(String genre, List<String> platforms) {
        List<PlatformStats> stats = new ArrayList<>();
        for (String platform : platforms) {
            PlatformStats platformStat = new PlatformStats();
            platformStat.setPlatformName(platform);
            platformStat.setTotalFilms(0);
            platformStat.setTotalSeries(0);

            Query query = new Query();
            query.addCriteria(Criteria.where("streamingPlatforms").is(platform));

            if (genre != null && !genre.isBlank()) {
                query.addCriteria(Criteria.where("genres").in(genre));
            }

            List<Show> shows = mongoTemplate.find(query, Show.class);
            for (Show show : shows) {
                if (show instanceof Film) {
                    platformStat.setTotalFilms(platformStat.getTotalFilms() + 1);
                } else if (show instanceof Series) {
                    platformStat.setTotalSeries(platformStat.getTotalSeries() + 1);
                }
            }
            stats.add(platformStat);
        }
        return stats;
    }
}
