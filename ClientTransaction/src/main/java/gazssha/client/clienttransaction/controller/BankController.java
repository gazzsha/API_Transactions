package gazssha.client.clienttransaction.controller;

import gazssha.client.clienttransaction.dto.BankAccountDto;
import gazssha.client.clienttransaction.dto.FullInformationTransactionDto;
import gazssha.client.clienttransaction.dto.LimitDto;
import gazssha.client.clienttransaction.entity.BankAccount;
import gazssha.client.clienttransaction.entity.Limits;
import gazssha.client.clienttransaction.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody @Valid BankAccountDto bankAccountDto) {
        return ResponseEntity.ok().body(bankService.createBankAccount(bankAccountDto));
    }

    @PostMapping(value = "/limits/{account_bank}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccount> updateLimits(@PathVariable(name = "account_bank") Long account, @RequestBody @Valid LimitDto limitDto) {
        return ResponseEntity.ok().body(bankService.updateLimits(account, limitDto));
    }

    @GetMapping(value = "/limits/{account_bank}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Limits>> getLimits(@PathVariable(name = "account_bank") Long account) {
        return ResponseEntity.ok().body(bankService.getLimitsByAccount(account));
    }

    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FullInformationTransactionDto>> getTransactions() {
        return ResponseEntity.ok().body(bankService.getFullInformationTransactionsDto());
    }
}
