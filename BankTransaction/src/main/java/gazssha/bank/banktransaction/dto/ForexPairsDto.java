package gazssha.bank.banktransaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ForexPairsDto(@NotNull List<ForexPairDto> data,
                            @NotBlank String status) {}
