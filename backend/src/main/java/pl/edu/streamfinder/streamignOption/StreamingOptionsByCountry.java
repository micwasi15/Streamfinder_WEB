package pl.edu.streamfinder.streamignOption;

import lombok.Data;

import java.util.List;

@Data
public class StreamingOptionsByCountry {
    private String country;
    List<Option> options;
}
