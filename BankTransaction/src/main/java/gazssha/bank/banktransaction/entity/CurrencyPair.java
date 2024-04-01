package gazssha.bank.banktransaction.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;


@Table
@Data
public class CurrencyPair {

    @PrimaryKey
    private String shortSymbol;

    @Column
    private String symbol;
    @Column
    private String exchange;
    @Column
    private LocalDate dateTime;
    @Column
    private Long timestamp;
    @Column
    private Double close;
}
