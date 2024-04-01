package gazssha.bank.banktransaction.utils;


import gazssha.bank.banktransaction.dto.CurrencyPairDto;
import gazssha.bank.banktransaction.dto.TransactionDto;
import gazssha.bank.banktransaction.entity.CurrencyPair;
import gazssha.bank.banktransaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Mapper
public interface PojoMapper {

    PojoMapper INSTANCE = Mappers.getMapper(PojoMapper.class);


    @Mapping(target = "accountFrom", source = "transactionDto.account_from")
    @Mapping(target = "accountTo", source = "transactionDto.account_to")
    @Mapping(target = "currencyShortname", source = "transactionDto.currency_shortname")
    @Mapping(target = "sum", source = "transactionDto.sum")
    @Mapping(target = "expenseCategory", source = "transactionDto.expense_category")
    @Mapping(target = "dateTime", source = "transactionDto.datetime", dateFormat = "yyyy-MM-dd HH:mm:ssX")
    @Mapping(ignore = true,target = "id")
    @Mapping(ignore = true, target = "limits")
    @Mapping(ignore = true, target = "limitExceeded")
    Transaction transactionDtoToTransaction(TransactionDto transactionDto);

    default CurrencyPair currencyPairDtpToEntity(CurrencyPairDto currencyPairDto) {
        CurrencyPair currencyPair = new CurrencyPair();
        currencyPair.setSymbol(currencyPairDto.symbol());
        currencyPair.setShortSymbol(currencyPairDto.symbol().split("/")[0]);
        currencyPair.setExchange(currencyPairDto.exchange());
        currencyPair.setDateTime(LocalDate.parse(currencyPairDto.datetime(), DateTimeFormatter.ISO_LOCAL_DATE));
        currencyPair.setTimestamp(currencyPairDto.timestamp());
        currencyPair.setClose(currencyPairDto.close());
        return currencyPair;
    }
}
