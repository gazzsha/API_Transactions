package gazssha.bank.banktransaction.scheduler;

import gazssha.bank.banktransaction.service.CurrencyPairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final CurrencyPairService currencyPairService;



//    @Scheduled(cron = "0 0 12 * * *")
//    public void UpdateClosePrice() {
//        currencyPairService.updateCurrencyPair();
//    }
}
