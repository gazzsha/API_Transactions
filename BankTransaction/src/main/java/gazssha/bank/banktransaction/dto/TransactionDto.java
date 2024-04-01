package gazssha.bank.banktransaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gazssha.bank.banktransaction.utils.Category;
import gazssha.bank.banktransaction.utils.JsonDateTimeDeserialize;
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

}
