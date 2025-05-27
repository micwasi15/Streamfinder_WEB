package pl.edu.streamfinder.streamingService;

import lombok.Data;

@Data
public class Plan {
    private String name;
    private PricesPerCountry pricesPerCountry;
}
