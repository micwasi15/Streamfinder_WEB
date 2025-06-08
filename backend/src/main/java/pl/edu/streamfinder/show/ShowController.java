package pl.edu.streamfinder.show;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/shows/type/{id}")
    public ResponseEntity<Map<String, String>> getShowType(@PathVariable String id) {
        String type = showService.getShowType(id);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("showType", type));
    }

    @GetMapping("/debug")
    public List<?> debugTypes() {
        return showService.debugTypes();
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

    @GetMapping("/shows/platform-stats")
    public ResponseEntity<List<PlatformStats>> getPlatformStats(@RequestParam(required = false) String genre, @RequestParam List<String> platforms) {
        List<PlatformStats> stats = showService.getPlatformStats(genre, platforms);
        return ResponseEntity.ok(stats);
    }
}
