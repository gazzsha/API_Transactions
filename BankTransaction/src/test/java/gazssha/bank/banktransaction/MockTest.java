package gazssha.bank.banktransaction;


import gazssha.bank.banktransaction.dto.TransactionDto;
import gazssha.bank.banktransaction.entity.BankAccount;
import gazssha.bank.banktransaction.entity.CurrencyPair;
import gazssha.bank.banktransaction.entity.Limits;
import gazssha.bank.banktransaction.entity.Transaction;
import gazssha.bank.banktransaction.repository.BankAccountRepository;
import gazssha.bank.banktransaction.repository.LimitRepository;
import gazssha.bank.banktransaction.repository.TransactionRepository;
import gazssha.bank.banktransaction.service.BankService;
import gazssha.bank.banktransaction.service.CurrencyPairService;
import gazssha.bank.banktransaction.utils.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockTest {


    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CurrencyPairService currencyPairService;


    @Mock
    private LimitRepository limitRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BankService bankService;

    @Test
    public void testMakeTransactionFlagFalse() {
        TransactionDto transactionDto = new TransactionDto(123L, 9999999999L, "RUB", 10000000.45,
                Category.PRODUCT, ZonedDateTime.now());
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setMoneyProduct(BigDecimal.valueOf(1000));
        CurrencyPair currencyPair = new CurrencyPair();
        currencyPair.setShortSymbol("RUB");
        currencyPair.setClose(11.3);
        Limits limits = new Limits();
        when(bankAccountRepository.findByAccount(any())).thenReturn(Optional.of(bankAccount));
        when(currencyPairService.getCurrencyPairByShortName(any())).thenReturn(Optional.of(currencyPair));
        when(limitRepository.findTopByBankAccountIdAndCategoryOrderByDateTime(anyLong(), any())).thenReturn(limits);
        bankService.makeTransaction(transactionDto);
        ArgumentCaptor<Transaction> argument = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).saveAndFlush(argument.capture());
        Transaction capturedTransaction = argument.getValue();
        verify(transactionRepository, times(1)).saveAndFlush(capturedTransaction);
        assertNotNull(capturedTransaction);
        assertEquals(capturedTransaction.getLimitExceeded(), true);
    }

    @Test
    public void testMakeTransactionFlagTrue() {
        TransactionDto transactionDto = new TransactionDto(123L, 9999999999L, "RUB", 10.2,
                Category.PRODUCT, ZonedDateTime.now());
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setMoneyProduct(BigDecimal.valueOf(1000));
        CurrencyPair currencyPair = new CurrencyPair();
        currencyPair.setShortSymbol("RUB");
        currencyPair.setClose(11.3);
        Limits limits = new Limits();
        when(bankAccountRepository.findByAccount(any())).thenReturn(Optional.of(bankAccount));
        when(currencyPairService.getCurrencyPairByShortName(any())).thenReturn(Optional.of(currencyPair));
        when(limitRepository.findTopByBankAccountIdAndCategoryOrderByDateTime(anyLong(), any())).thenReturn(limits);
        bankService.makeTransaction(transactionDto);
        ArgumentCaptor<Transaction> argument = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).saveAndFlush(argument.capture());
        Transaction capturedTransaction = argument.getValue();
        verify(transactionRepository, times(1)).saveAndFlush(capturedTransaction);
        assertNotNull(capturedTransaction);
        assertEquals(false, capturedTransaction.getLimitExceeded());
    }

}