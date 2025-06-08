package pl.edu.streamfinder.countries;

import lombok.Data;

@Data
public class CountryDTO {
    private String code;
    private String name;

    public CountryDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
