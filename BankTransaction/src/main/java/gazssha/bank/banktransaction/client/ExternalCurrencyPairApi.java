package gazssha.bank.banktransaction.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-pair")
public interface ExternalCurrencyPairApi {

    @GetMapping
    @CircuitBreaker(name = "currency-pair-breaker")
    @Retry(name = "currency-pair-retry")
    ResponseEntity<String> getResponse(@RequestParam(name = "symbol") String symbol,
                                       @RequestParam(name = "apikey") String secretKey);
}
