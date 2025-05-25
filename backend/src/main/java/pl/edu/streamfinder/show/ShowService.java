package pl.edu.streamfinder.show;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {
    private final ShowRepository showRepository;

    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public List<Show> findAll() {
        return showRepository.findAll();
    }

    public List<?> debugTypes() {
        return showRepository.findAll().stream()
                .map(Object::getClass)
                .toList();
    }
}
