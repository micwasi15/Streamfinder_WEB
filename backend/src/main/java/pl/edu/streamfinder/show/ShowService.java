package pl.edu.streamfinder.show;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.edu.streamfinder.streamignOption.StreamingOptionsService;
import pl.edu.streamfinder.user.User;
import pl.edu.streamfinder.user.UserService;

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
    private final UserService userService;

    public ShowService(ShowRepository showRepository, MongoTemplate mongoTemplate, StreamingOptionsService streamingOptionsService, UserService userService) {
        this.showRepository = showRepository;
        this.mongoTemplate = mongoTemplate;
        this.streamingOptionsService = streamingOptionsService;
        this.userService = userService;
    }

    public boolean isShowFavorite(String id, String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return false;
        }
        return user.getFavoriteShowIds().contains(id);
    }

    public boolean addShowToFavorites(String id, String email) {
        return userService.addShowToFavorites(id, email);
    }

    public boolean removeShowFromFavorites(String id, String email) {
        return userService.removeShowFromFavorites(id, email);
    }

    public Page<Show> searchUserFavoriteShows(ShowSearchCriteria criteria, String email) {
        List<String> favoriteShowIds = userService.findByEmail(email).getFavoriteShowIds();

        if (favoriteShowIds.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(criteria.getPage(), criteria.getSize()), 0);
        }

        return searchShows(criteria, favoriteShowIds);
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
            filters.add(Criteria.where("title").regex(".*" + criteria.getTitle().replaceAll("(?<=.)(?=.)", ".*") + ".*", "i"));
        }

        if (criteria.getGenres() != null && !criteria.getGenres().isEmpty()) {
            filters.add(Criteria.where("genres").in(criteria.getGenres()));
        }

        if (criteria.getActors() != null && !criteria.getActors().isEmpty()) {
            List<Criteria> actorRegexCriterias = new ArrayList<>();
            for (String actor : criteria.getActors()) {
                actorRegexCriterias.add(Criteria.where("cast").regex(".*" + Pattern.quote(actor) + ".*", "i"));
            }
            filters.add(new Criteria().orOperator(actorRegexCriterias.toArray(new Criteria[0])));
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

        if (criteria.getMinRating() != null) {
            filters.add(Criteria.where("rating").gte(criteria.getMinRating()));
        }

        List<Criteria> filmCriteriaList = new ArrayList<>();
        List<Criteria> seriesCriteriaList = new ArrayList<>();

        filmCriteriaList.add(Criteria.where("_class").is("film"));
        seriesCriteriaList.add(Criteria.where("_class").is("series"));

        if (criteria.getMinYear() != null) {
            filmCriteriaList.add(Criteria.where("releaseYear").gte(criteria.getMinYear()));
            seriesCriteriaList.add(Criteria.where("firstAirYear").gte(criteria.getMinYear()));
        }

        if (criteria.getMaxYear() != null) {
            filmCriteriaList.add(Criteria.where("releaseYear").lte(criteria.getMaxYear()));
            seriesCriteriaList.add(Criteria.where("lastAirYear").lte(criteria.getMaxYear()));
        }

        Criteria filmCriteria = new Criteria().andOperator(filmCriteriaList.toArray(new Criteria[0]));
        Criteria seriesCriteria = new Criteria().andOperator(seriesCriteriaList.toArray(new Criteria[0]));

        filters.add(new Criteria().orOperator(filmCriteria, seriesCriteria));


        Query query = new Query();

        query.addCriteria(new Criteria().andOperator(filters.toArray(new Criteria[0])));

        long total = mongoTemplate.count(query, Show.class);
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getSortBy()));
        query.with(pageable);

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

    public String getShowType(String id) {
        Show show = showRepository.findById(id).orElse(null);
        if (show == null) {
            return null;
        }
        return show.getShowType();
    }

    public List<String> getAllStreamingPlatforms() {
        List<Show> shows = showRepository.findAll();
        Set<String> platforms = new HashSet<>();
        for (Show show : shows) {
            if (show.getStreamingPlatforms() != null) {
                platforms.addAll(show.getStreamingPlatforms());
            }
        }
        List<String> res = new ArrayList<>(platforms);
        List<PlatformStats> platformStats = getPlatformStats(null, res);
        res.sort((p1, p2) -> {
            int count1 = platformStats.stream().filter(ps -> ps.getPlatformName().equals(p1)).mapToInt(PlatformStats::getTotalFilms).sum() +
                    platformStats.stream().filter(ps -> ps.getPlatformName().equals(p1)).mapToInt(PlatformStats::getTotalSeries).sum();
            int count2 = platformStats.stream().filter(ps -> ps.getPlatformName().equals(p2)).mapToInt(PlatformStats::getTotalFilms).sum() +
                    platformStats.stream().filter(ps -> ps.getPlatformName().equals(p2)).mapToInt(PlatformStats::getTotalSeries).sum();
            return Integer.compare(count2, count1);
        });

        return res;
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
