package gazssha.client.clienttransaction.service;


import gazssha.client.clienttransaction.dto.BankAccountDto;
import gazssha.client.clienttransaction.dto.FullInformationTransactionDto;
import gazssha.client.clienttransaction.dto.LimitDto;
import gazssha.client.clienttransaction.dto.TransactionDto;
import gazssha.client.clienttransaction.entity.BankAccount;
import gazssha.client.clienttransaction.entity.Limits;
import gazssha.client.clienttransaction.entity.Transaction;
import gazssha.client.clienttransaction.exception.ClientException;
import gazssha.client.clienttransaction.exception.NotFoundBankAccount;
import gazssha.client.clienttransaction.repository.BankAccountRepository;
import gazssha.client.clienttransaction.repository.TransactionRepository;
import gazssha.client.clienttransaction.utils.Category;
import gazssha.client.clienttransaction.utils.PojoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BankAccount createBankAccount(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = PojoMapper.INSTANCE.bakAccountDtoToBankAccount(bankAccountDto);
        if (bankAccountRepository.findByAccount(bankAccount.getAccount()).isPresent())
            throw new ClientException(String.format("Bank Account already exist with Account %s", bankAccount.getAccount().toString()));
        return bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BankAccount updateLimits(Long account, LimitDto limitDto) {
        BankAccount bankAccount = bankAccountRepository.findByAccount(account).orElseThrow(
                () -> new NotFoundBankAccount(String.format("Bank Account with Account %s doesnt exist", account))
        );
        Limits limits = PojoMapper.INSTANCE.limitDtoToLimits(limitDto);
        String patternDateTime = "yyy-MM-dd HH:mm:ssX";
        limits.setDateTime(ZonedDateTime.parse(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(patternDateTime)), DateTimeFormatter.ofPattern(patternDateTime)));
        bankAccount.addLimit(limits);
        Category category = limits.getCategory();
        if (category == Category.PRODUCT) {
            bankAccount.setMoneyProduct(bankAccount.getMoneyProduct().add(limits.getLimitSum()));
        } else {
            bankAccount.setMoneyService(bankAccount.getMoneyService().add(limits.getLimitSum()));
        }
        return bankAccount;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Limits> getLimitsByAccount(Long account) {
        BankAccount bankAccount = bankAccountRepository.findByAccount(account).orElseThrow(
                () -> new NotFoundBankAccount(String.format("Bank Account with Account %s doesnt exist", account))
        );
        return bankAccount.getLimits();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<FullInformationTransactionDto> getFullInformationTransactionsDto() {
        List<Transaction> transactions = transactionRepository.findAllByLimits();
        return transactions.stream()
                .map(transaction ->
                        new FullInformationTransactionDto(new TransactionDto(transaction.getAccountFrom(),
                                transaction.getAccountTo(), transaction.getCurrencyShortname(),
                                transaction.getSum().setScale(2, RoundingMode.HALF_UP).doubleValue(), transaction.getExpenseCategory(), transaction.getDateTime()), transaction.getLimits().getLimitSum().setScale(2, RoundingMode.HALF_UP).doubleValue(),
                                transaction.getLimits().getDateTime
                                        (), "USD"))
                .collect(Collectors.toList());
    }

}
