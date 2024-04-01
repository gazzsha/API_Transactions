package gazssha.bank.banktransaction.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gazssha.bank.banktransaction.client.ExternalCurrenciesApi;
import gazssha.bank.banktransaction.client.ExternalCurrencyPairApi;
import gazssha.bank.banktransaction.dto.CurrencyPairDto;
import gazssha.bank.banktransaction.dto.ForexPairDto;
import gazssha.bank.banktransaction.dto.ForexPairsDto;
import gazssha.bank.banktransaction.entity.CurrencyPair;
import gazssha.bank.banktransaction.exception.ServerError;
import gazssha.bank.banktransaction.repository.CurrencyPairRepository;
import gazssha.bank.banktransaction.utils.PojoMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@PropertySource("classpath:application.yml")
public class CurrencyPairService {

    private final Environment environment;

    private final CurrencyPairRepository currencyPairRepository;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    private final ExternalCurrencyPairApi externalCurrencyPairApi;

    private final ExternalCurrenciesApi externalCurrenciesApi;

    private Map<String, String> symbols;

//    @PostConstruct
//    public void mapper() {
//        symbols = currencyPairRepository.findAll()
//                .stream().map(CurrencyPair::getSymbol)
//                .collect(Collectors.toMap(sym -> sym.split("/")[0],
//                        sym -> sym));
//    }

    public Optional<CurrencyPair> getCurrencyPairByShortName(String shortName) {
        return Optional.ofNullable(currencyPairRepository.findCurrencyPairByShortSymbol(shortName));
    }

    //
    @PostConstruct
    private void getAllForexPairs() {
        ForexPairsDto dataForex = getExternalCurrenciesApiData();
        if (!Objects.isNull(dataForex) && !Objects.equals(dataForex.status(), HttpStatus.OK.toString()) && !dataForex.data().isEmpty()) {
            symbols = dataForex.data().stream()
                    .map(ForexPairDto::symbol)
                    .collect(Collectors.toMap(forexDto -> forexDto.split("/")[0],
                            forexDto -> forexDto));
            updateCurrencyPair();
        } else {
            throw new ServerError("Server response status not OK");
        }
    }

    public void updateCurrencyPair() {
        ExecutorService executor = new ThreadPoolExecutor(7, 7, 2, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(8), new ThreadPoolExecutor.CallerRunsPolicy());
        List<UpdateCurrencyPairTask> runnableTask = symbols.values().stream()
                .map(UpdateCurrencyPairTask::new)
                .toList();
        for (var task : runnableTask) {
            executor.execute(() -> {
                task.run();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ignore) {
                }
            });
        }
    }


    class UpdateCurrencyPairTask implements Runnable {

        private final String symbol;

        public UpdateCurrencyPairTask(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public void run() {
//            ResponseEntity<String> response = restTemplate.getForEntity(GET_CURRENCY_PAIR, String.class, Map.of("symbol", symbol,
//                    "secret_key", SECRET_KEY));
            ResponseEntity<String> response = getExternalResponseApiCurrencyPair(symbol, environment.getProperty("secret-key"));
            LinkedHashMap responseJson = null;
            try {
                responseJson = (LinkedHashMap) objectMapper.readValue(response.getBody(), Object.class);
            } catch (JsonProcessingException e) {
                throw new ServerError("Parsing error");
            }
            if (response.getStatusCode().is5xxServerError() ||
                    response.getStatusCode().is4xxClientError() ||
                    (responseJson.containsKey("code") && (
                            HttpStatus.valueOf((Integer) responseJson.get("code")).is4xxClientError()
                                    || HttpStatus.valueOf((Integer) responseJson.get("code")).is5xxServerError())))
                throw new ServerError(response.getBody());
            try {
                log.info(response.getBody());
                currencyPairRepository.save(PojoMapper.INSTANCE.currencyPairDtpToEntity(objectMapper.readValue(response.getBody(), CurrencyPairDto.class)));
            } catch (JsonProcessingException ignore) {
            }
        }
    }


    private ForexPairsDto getExternalCurrenciesApiData() {
        return externalCurrenciesApi.getForexPairsApi();
    }

    private ResponseEntity<String> getExternalResponseApiCurrencyPair(final String symbol, final String secretKey) {
        return externalCurrencyPairApi.getResponse(symbol, secretKey);
    }

}


