package gazssha.client.clienttransaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gazssha.client.clienttransaction.utils.JsonDateTimeDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
public class FullInformationTransactionDto extends TransactionDto{
    private final Double limit_sum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssX")
    @JsonDeserialize(using = JsonDateTimeDeserialize.class)
    private final ZonedDateTime limit_datetime;
    private final String limit_currency_shortname;

    public FullInformationTransactionDto(TransactionDto transactionDto, Double limit_sum, ZonedDateTime limit_datetime, String limit_currency_shortname) {
        super(transactionDto);
        this.limit_sum = limit_sum;
        this.limit_datetime = limit_datetime;
        this.limit_currency_shortname = limit_currency_shortname;
    }
}
