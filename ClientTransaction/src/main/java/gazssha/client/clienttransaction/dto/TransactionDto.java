package gazssha.client.clienttransaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gazssha.client.clienttransaction.utils.Category;
import gazssha.client.clienttransaction.utils.JsonDateTimeDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @NotNull
    Long account_from;
    @NotNull
    Long account_to;
    @NotBlank @Length(min = 3, max = 3)
    String currency_shortname;
    @NotNull
    Double sum;
    @NotNull
    Category expense_category;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssX")
    @JsonDeserialize(using = JsonDateTimeDeserialize.class)
    ZonedDateTime datetime;

    public TransactionDto(TransactionDto transactionDto) {
        this.account_from = transactionDto.getAccount_from();
        this.account_to = transactionDto.getAccount_to();
        this.sum = transactionDto.getSum();
        this.datetime = transactionDto.getDatetime();
        this.expense_category = transactionDto.getExpense_category();
        this.currency_shortname = transactionDto.getCurrency_shortname();
    }
}
