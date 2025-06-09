package pl.edu.streamfinder.currency;

import lombok.Data;

@Data
public class CurrencyExchange {
    private String baseCurrency;
    private Float euroExchangeRate;
    private Float usdExchangeRate;
    private Float plnExchangeRate;
}
