package gazssha.bank.banktransaction.controller;


import gazssha.bank.banktransaction.dto.TransactionDto;
import gazssha.bank.banktransaction.entity.Transaction;
import gazssha.bank.banktransaction.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping(value = "/do", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> makeTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        return ResponseEntity.ok().body(bankService.makeTransaction(transactionDto));
    }
}
