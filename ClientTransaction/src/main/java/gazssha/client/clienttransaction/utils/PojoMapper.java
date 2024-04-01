package gazssha.client.clienttransaction.utils;

import gazssha.client.clienttransaction.dto.BankAccountDto;
import gazssha.client.clienttransaction.dto.LimitDto;
import gazssha.client.clienttransaction.dto.TransactionDto;
import gazssha.client.clienttransaction.entity.BankAccount;
import gazssha.client.clienttransaction.entity.Limits;
import gazssha.client.clienttransaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Mapper
public interface PojoMapper {

    PojoMapper INSTANCE = Mappers.getMapper(PojoMapper.class);


    @Mapping(target = "account", source = "bankAccount.account")
    @Mapping(target = "limits", source = "bankAccount.limits")
    BankAccountDto bankAccountToBankAccountDto(BankAccount bankAccount);

    @Mapping(target = "limit", source = "limits.limitSum")
    @Mapping(target = "category", source = "limits.category")
    @Mapping(target = "dateTime", source = "limits.dateTime")
    LimitDto limitsToLimitDto(Limits limits);

    @Mapping(target = "account_from", source = "transaction.accountFrom")
    @Mapping(target = "account_to", source = "transaction.accountTo")
    @Mapping(target = "currency_shortname", source = "transaction.currencyShortname")
    @Mapping(target = "sum", source = "transaction.sum")
    @Mapping(target = "expense_category", source = "transaction.expenseCategory")
    @Mapping(target = "datetime", source = "transaction.dateTime", dateFormat = "yyyy-MM-dd HH:mm:ssX")
    TransactionDto transactionToTransactionDto(Transaction transaction);


    @Mapping(target = "limitSum", source = "limitDto.limit", defaultValue = "1000.0")
    @Mapping(target = "category", source = "limitDto.category")
    @Mapping(target = "dateTime", source = "dateTime", defaultExpression = "java(ZonedDateTime.now())", dateFormat = "yyyy-MM-dd HH:mm:ssX")
    Limits limitDtoToLimits(LimitDto limitDto);

    default BankAccount bakAccountDtoToBankAccount(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccount(bankAccountDto.account());
        if (Objects.nonNull(bankAccountDto.limits())) {
            bankAccountDto.limits().stream()
                    .map(PojoMapper.INSTANCE::limitDtoToLimits)
                    .forEach(bankAccount::addLimit);
            bankAccount.getLimits()
                    .forEach(limit -> {
                        if (Category.SERVICE == limit.getCategory()) {
                            bankAccount.setMoneyService(Objects.requireNonNullElse(limit.getLimitSum(), BigDecimal.valueOf(1000.0)));
                        } else {
                            bankAccount.setMoneyProduct(Objects.requireNonNullElse(limit.getLimitSum(), BigDecimal.valueOf(1000.0)));
                        }
                    });
        }
        if (Objects.isNull(bankAccount.getMoneyService())) {
            setDefaultLimits(bankAccount, Category.SERVICE);
            bankAccount.setMoneyService(BigDecimal.valueOf(1000L));
        }
        if (Objects.isNull(bankAccount.getMoneyProduct())) {
            setDefaultLimits(bankAccount, Category.PRODUCT);
            bankAccount.setMoneyProduct(BigDecimal.valueOf(1000L));
        }
        return bankAccount;
    }

    private void setDefaultLimits(BankAccount bankAccount, Category category) {
        Limits defaultLimit = new Limits();
        defaultLimit.setLimitSum(BigDecimal.valueOf(1000L));
        String patternDateTime = "yyy-MM-dd HH:mm:ssX";
        defaultLimit.setDateTime(ZonedDateTime.parse(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(patternDateTime)), DateTimeFormatter.ofPattern(patternDateTime)));
        defaultLimit.setCategory(category);
        bankAccount.addLimit(defaultLimit);
    }


}
