package pl.edu.streamfinder.currency;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {
    private final CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("/api/public/currency/latest")
    public ResponseEntity<CurrencyExchangeData> getLatestCurrencyExchangeData() {
        CurrencyExchangeData latestData = currencyExchangeService.findLatestCurrencyExchangeData();
        if (latestData == null) {
            throw new IllegalStateException("No currency exchange data available");
        }
        return ResponseEntity.ok(latestData);
    }
}
