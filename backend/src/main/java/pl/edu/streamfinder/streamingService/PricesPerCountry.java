package pl.edu.streamfinder.streamingService;

import lombok.Data;

import java.util.Date;

@Data
public class PricesPerCountry {
    private String countryCode;
    private String countryName;
    private double price;
    private String currency;
    private String ISODate;
}
