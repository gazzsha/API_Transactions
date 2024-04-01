package gazssha.bank.banktransaction.dto;

import jakarta.validation.constraints.NotBlank;

public record ForexPairDto(@NotBlank String symbol,

                           @NotBlank String currency_group,
                           @NotBlank String currency_base,
                           @NotBlank String currency_quote
) {

}
