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

    @GetMapping("public/api/currency/latest")
    public ResponseEntity<CurrencyExchangeData> getLatestCurrencyExchangeData() {
        CurrencyExchangeData latestData = currencyExchangeService.findLatestCurrencyExchangeData();
        if (latestData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestData);
    }
}
