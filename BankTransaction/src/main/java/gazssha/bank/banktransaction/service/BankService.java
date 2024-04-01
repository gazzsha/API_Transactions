package gazssha.bank.banktransaction.service;

import gazssha.bank.banktransaction.dto.TransactionDto;
import gazssha.bank.banktransaction.entity.BankAccount;
import gazssha.bank.banktransaction.entity.CurrencyPair;
import gazssha.bank.banktransaction.entity.Transaction;
import gazssha.bank.banktransaction.exception.ClientException;
import gazssha.bank.banktransaction.exception.NotFoundBankAccount;
import gazssha.bank.banktransaction.repository.BankAccountRepository;
import gazssha.bank.banktransaction.repository.LimitRepository;
import gazssha.bank.banktransaction.repository.TransactionRepository;
import gazssha.bank.banktransaction.utils.Category;
import gazssha.bank.banktransaction.utils.PojoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyPairService currencyPairService;
    private final LimitRepository limitRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Transaction makeTransaction(TransactionDto transactionDto) {
        Transaction transaction = PojoMapper.INSTANCE.transactionDtoToTransaction(transactionDto);
        BankAccount bankAccount = bankAccountRepository.findByAccount(transaction.getAccountFrom()).orElseThrow(
                () -> new NotFoundBankAccount(String.format("Account bank with ID %s not found", transaction.getAccountFrom()))
        );
        CurrencyPair currencyPair = currencyPairService.getCurrencyPairByShortName(transaction.getCurrencyShortname()).orElseThrow(
                () -> new ClientException(String.format("Not found currency with name %s", transaction.getCurrencyShortname()))
        );
        Category expenseCategory = transaction.getExpenseCategory();
        BigDecimal currentMoney = null;
        if (expenseCategory == Category.SERVICE) {
            currentMoney = bankAccount.getMoneyService();
        } else if (expenseCategory == Category.PRODUCT) {
            currentMoney = bankAccount.getMoneyProduct();
        } else {
            throw new ClientException(String.format("Not found category with name %s", expenseCategory));
        }
        BigDecimal amountTransferred = transaction.getSum();
        BigDecimal amountIncludingExchangeRate = amountTransferred.multiply(BigDecimal.valueOf(currencyPair.getClose()));
        currentMoney = currentMoney.subtract(amountIncludingExchangeRate).setScale(2, RoundingMode.HALF_UP);
        Boolean currentFlag = currentMoney.compareTo(BigDecimal.ZERO) < 0;
        transaction.setLimitExceeded(currentFlag);
        transaction.setLimits(limitRepository.findTopByBankAccountIdAndCategoryOrderByDateTime(bankAccount.getId(), expenseCategory));
        if (expenseCategory == Category.PRODUCT) {
            bankAccount.setMoneyProduct(currentMoney);
        } else {
            bankAccount.setMoneyService(currentMoney);
        }
        bankAccountRepository.saveAndFlush(bankAccount);
        return transactionRepository.saveAndFlush(transaction);
    }

}
