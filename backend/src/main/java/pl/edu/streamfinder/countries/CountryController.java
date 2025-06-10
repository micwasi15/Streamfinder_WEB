package pl.edu.streamfinder.countries;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CountryController {
    @GetMapping("/api/public/countries")
    public ResponseEntity<List<CountryDTO>> getCountries() {
        List<CountryDTO> countries = Arrays.stream(Country.values())
                .map(c -> new CountryDTO(c.getCode(), c.getFullName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(countries);
    }

}
