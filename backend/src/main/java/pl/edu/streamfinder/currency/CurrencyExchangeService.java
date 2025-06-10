package pl.edu.streamfinder.currency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class CurrencyExchangeService {
    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final RestTemplate restTemplate;

    @Value("${currency.exchange.api-key}")
    private String apiKey;

    public CurrencyExchangeService(CurrencyExchangeRepository currencyExchangeRepository, RestTemplate restTemplate) {
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.restTemplate = restTemplate;
    }

    public CurrencyExchangeData findLatestCurrencyExchangeData() {
        CurrencyExchangeData res = currencyExchangeRepository.findTopByOrderByTimestampDesc();
        int hour = 60 * 60;
        long currentTimestamp = System.currentTimeMillis() / 1000;
        if (res == null || currentTimestamp - res.getTimestamp() > hour) {
            updateCurrencyExchangeData();
            return currencyExchangeRepository.findTopByOrderByTimestampDesc();
        }
        return res;
    }

    public void updateCurrencyExchangeData() {
        long currentTimestamp = System.currentTimeMillis() / 1000;
        Map<String, Double> usdExchangeRates;
        try {
            usdExchangeRates = getRatesForCurrency();
        } catch (RuntimeException e) {
            return;
        }

        CurrencyExchangeData currencyExchangeData = new CurrencyExchangeData();
        currencyExchangeData.setTimestamp(currentTimestamp);
        Set<String> currencies = usdExchangeRates.keySet();

        double defaultExchangeRate = 1.0;
        float euroExchangeRate = usdExchangeRates.getOrDefault("EUR", defaultExchangeRate).floatValue();
        float plnExchangeRate = usdExchangeRates.getOrDefault("PLN", defaultExchangeRate).floatValue();

        for (String currency : currencies) {
            CurrencyExchange currencyExchange = new CurrencyExchange();
            currencyExchange.setBaseCurrency(currency);
            float usdExchangeRate = usdExchangeRates.getOrDefault(currency, defaultExchangeRate).floatValue();
            currencyExchange.setUsdExchangeRate(usdExchangeRate);
            currencyExchange.setEuroExchangeRate(usdExchangeRate / euroExchangeRate);
            currencyExchange.setPlnExchangeRate(usdExchangeRate / plnExchangeRate);
            currencyExchangeData.getCurrencyExchanges().add(currencyExchange);
        }

        currencyExchangeRepository.save(currencyExchangeData);
    }

    public Map<String, Double> getRatesForCurrency() throws RuntimeException {
        String url = "https://api.currencyfreaks.com/v2.0/rates/latest?apikey=" + apiKey;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to fetch exchange rates: " + response.getStatusCode());
        }
        Map<String, Object> body = response.getBody();

        Map<String, String> rates = (Map<String, String>) body.get("rates");
        Map<String, Double> exchangeRates = new HashMap<>();
        if (rates == null) {
            throw new RuntimeException("No rates found in the response");
        }
        for (Map.Entry<String, String> entry : rates.entrySet()) {
            try {
                exchangeRates.put(entry.getKey(), Double.parseDouble(entry.getValue()));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid rate value for currency: " + entry.getKey());
            }
        }

        return exchangeRates;
    }
}
