package gazssha.client.clienttransaction;


import gazssha.client.clienttransaction.dto.BankAccountDto;
import gazssha.client.clienttransaction.dto.LimitDto;
import gazssha.client.clienttransaction.entity.BankAccount;
import gazssha.client.clienttransaction.entity.Limits;
import gazssha.client.clienttransaction.exception.ClientException;
import gazssha.client.clienttransaction.service.BankService;
import gazssha.client.clienttransaction.utils.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class TestClientService {


    @Autowired
    private BankService bankService;

    @Test
    void testCreateBankAccount() {
        BankAccountDto bankAccountDto = new BankAccountDto(1L, null);
        BankAccount bankAccount = bankService.createBankAccount(bankAccountDto);
        Assertions.assertEquals(bankAccount.getAccount(), 1L);
        Assertions.assertEquals(bankAccount.getMoneyService(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(bankAccount.getMoneyProduct(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(bankAccount.getLimits().size(), 2);
        List<Limits> limits = bankAccount.getLimits();
        limits.forEach((limit) -> Assertions.assertEquals(limit.getLimitSum(), BigDecimal.valueOf(1000)));
    }


    @Test
    void testGetDefaultLimits() {
        BankAccountDto bankAccountDto = new BankAccountDto(2L, null);
        BankAccount bankAccount = bankService.createBankAccount(bankAccountDto);
        Assertions.assertEquals(bankAccount.getAccount(), 2L);
        Assertions.assertEquals(bankAccount.getMoneyService(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(bankAccount.getMoneyProduct(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(bankAccount.getLimits().size(), 2);
        List<Limits> limits = bankAccount.getLimits();
        limits.forEach((limit) -> {
            Assertions.assertEquals(limit.getLimitSum(), BigDecimal.valueOf(1000));
            Assertions.assertEquals(bankAccount, limit.getBankAccount());
        });
    }

    @Test
    void testNoneDefaultLimit() {
        LimitDto limitDto = new LimitDto(500d, Category.PRODUCT, ZonedDateTime.now());
        ArrayList<LimitDto> objects = new ArrayList<>();
        objects.add(limitDto);
        BankAccountDto bankAccountDto = new BankAccountDto(3L, objects);
        BankAccount bankAccount = bankService.createBankAccount(bankAccountDto);
        Assertions.assertEquals(bankAccount.getAccount(), 3L);
        Assertions.assertEquals(bankAccount.getMoneyService(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(bankAccount.getMoneyProduct(), BigDecimal.valueOf(500D));
        Assertions.assertEquals(bankAccount.getLimits().size(), 2);
        List<Limits> limits = bankAccount.getLimits();
        Limits returnedLimitsProduct = null;
        Limits returnedLimitsService = null;
        for (var elem : limits) {
            if (elem.getCategory() == Category.PRODUCT)
                returnedLimitsProduct = elem;
            else returnedLimitsService = elem;
        }
        Assertions.assertEquals(returnedLimitsProduct.getLimitSum().doubleValue(), 500D);
        Assertions.assertEquals(returnedLimitsProduct.getBankAccount(), bankAccount);
        Assertions.assertEquals(returnedLimitsService.getLimitSum(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(returnedLimitsService.getBankAccount(), bankAccount);
    }

    @Test
    void testThrowCreateAccount() {
        LimitDto limitDto = new LimitDto(500d, Category.PRODUCT, ZonedDateTime.now());
        ArrayList<LimitDto> objects = new ArrayList<>();
        objects.add(limitDto);
        BankAccountDto bankAccountDto = new BankAccountDto(100L, objects);
        bankService.createBankAccount(bankAccountDto);
        Assertions.assertThrows(ClientException.class, () -> bankService.createBankAccount(bankAccountDto));

    }

    @Test
    void testGetLimitsAndUpdate() {
        BankAccountDto bankAccountDto = new BankAccountDto(101L, null);
        bankService.createBankAccount(bankAccountDto);
        Assertions.assertThrows(ClientException.class, () -> bankService.createBankAccount(bankAccountDto));
        List<Limits> limitsByAccount = bankService.getLimitsByAccount(101L);
        Assertions.assertEquals(limitsByAccount.size(), 2);
        LimitDto limitDto = new LimitDto(0D, Category.PRODUCT, ZonedDateTime.now());
        BankAccount bankAccount = bankService.updateLimits(101L, limitDto);
        Assertions.assertEquals(bankAccount.getLimits().size(), 3);
        Limits limits = bankAccount.getLimits().get(2);
        Assertions.assertEquals(limits.getLimitSum().doubleValue(), BigDecimal.ZERO.doubleValue());
        Assertions.assertEquals(limits.getCategory(), Category.PRODUCT);
        Assertions.assertEquals(limits.getBankAccount(), bankAccount);
    }
}
