package gazssha.bank.banktransaction.client;


import gazssha.bank.banktransaction.dto.ForexPairsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "currencies")
public interface ExternalCurrenciesApi {


    @GetMapping
    ForexPairsDto getForexPairsApi();
}
