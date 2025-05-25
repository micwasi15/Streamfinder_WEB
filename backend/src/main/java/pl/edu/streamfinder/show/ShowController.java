package pl.edu.streamfinder.show;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowController {
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/shows")
    public ResponseEntity<List<Show>> getAllShows() {
        return ResponseEntity.ok(showService.findAll());
    }

    @GetMapping("/debug")
    public List<?> debugTypes() {
        return showService.debugTypes();
    }

}
