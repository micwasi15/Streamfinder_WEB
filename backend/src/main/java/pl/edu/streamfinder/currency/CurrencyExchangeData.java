package pl.edu.streamfinder.currency;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "currencyExchanges")
@Data
public class CurrencyExchangeData {
    @Id
    private String id;
    private List<CurrencyExchange> currencyExchanges = new ArrayList<>();
    private long timestamp;
}
