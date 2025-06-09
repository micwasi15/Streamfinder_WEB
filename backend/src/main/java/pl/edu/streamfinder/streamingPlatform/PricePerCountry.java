package pl.edu.streamfinder.streamingPlatform;

import lombok.Data;

@Data
public class PricePerCountry {
    private String countryCode;
    private String countryName;
    private double price;
    private String currency;
}
