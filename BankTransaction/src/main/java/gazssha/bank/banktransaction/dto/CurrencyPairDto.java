package gazssha.bank.banktransaction.dto;


public record CurrencyPairDto(String symbol, String exchange,
                              String datetime, Long timestamp,
                              Double close) {
}
