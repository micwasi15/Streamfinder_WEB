package pl.edu.streamfinder.show;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/shows/search")
    public ResponseEntity<Page<Show>> getAllShows(ShowSearchCriteria criteria) {
        return ResponseEntity.ok(showService.searchShows(criteria));
    }

    @GetMapping("/shows/favorites")
    public ResponseEntity<Page<Show>> getFavoriteShows(ShowSearchCriteria criteria, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        Page<Show> shows = showService.searchUserFavoriteShows(criteria, authentication.getName());
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/shows/favorites/{id}")
    public ResponseEntity<Map<String, Boolean>> isShowFavorite(@PathVariable String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        boolean isFavorite = showService.isShowFavorite(id, authentication.getName());
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }

    @PostMapping("/shows/favorites/{id}")
    public ResponseEntity<Void> addShowToFavorites(@PathVariable String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        boolean added = showService.addShowToFavorites(id, authentication.getName());
        if (added) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/shows/favorites/{id}")
    public ResponseEntity<Void> removeShowFromFavorites(@PathVariable String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        boolean removed = showService.removeShowFromFavorites(id, authentication.getName());
        if (removed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/shows/type/{id}")
    public ResponseEntity<Map<String, String>> getShowType(@PathVariable String id) {
        String type = showService.getShowType(id);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("showType", type));
    }

    @GetMapping("/shows/film/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable String id) {
        Film film = showService.getFilmById(id);
        if (film == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(film);
    }

    @GetMapping("/shows/series/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable String id) {
        Series series = showService.getSeriesById(id);
        if (series == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(series);
    }

    @GetMapping("/shows/platforms")
    public ResponseEntity<List<String>> getAllStreamingPlatforms() {
        List<String> platforms = showService.getAllStreamingPlatforms();
        return ResponseEntity.ok(platforms);
    }

    @GetMapping("/shows/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = showService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/shows/platform-stats")
    public ResponseEntity<List<PlatformStats>> getPlatformStats(@RequestParam(required = false) String genre, @RequestParam List<String> platforms) {
        List<PlatformStats> stats = showService.getPlatformStats(genre, platforms);
        return ResponseEntity.ok(stats);
    }
}
