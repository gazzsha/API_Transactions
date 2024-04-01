package gazssha.client.clienttransaction.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BankAccountDto(@NotNull Long account,
                             List<LimitDto> limits) {}
