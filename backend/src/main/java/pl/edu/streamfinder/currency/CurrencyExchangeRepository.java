package pl.edu.streamfinder.currency;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CurrencyExchangeRepository extends MongoRepository<CurrencyExchangeData, String> {
    CurrencyExchangeData findTopByOrderByTimestampDesc();
}
