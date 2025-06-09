package pl.edu.streamfinder.streamingPlatform;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Plan {
    private String name;
    private List<PricePerCountry> pricesPerCountry = new ArrayList<>();
}
