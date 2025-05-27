package pl.edu.streamfinder.show;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/shows")
    public ResponseEntity<Page<Show>> getAllShows(ShowSearchCriteria criteria) {
        return ResponseEntity.ok(showService.searchShows(criteria));
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
}
