package gazssha.client.clienttransaction.entity;

import gazssha.client.clienttransaction.utils.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "account_from", nullable = false)
    private Long accountFrom;

    @NotNull
    @Column(name = "account_to", nullable = false)
    private Long accountTo;

    @NotNull
    @Column(name = "currency_shortname", nullable = false)
    private String currencyShortname;

    @NotNull
    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @NotNull
    @Column(name = "expense_category", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category expenseCategory;

    @NotNull
    @Column(name = "date_time", nullable = false)
    @Basic
    private ZonedDateTime dateTime;

    @NotNull
    @Column(name = "limit_exceeded", nullable = false)
    private Boolean limitExceeded;

    @OneToOne
    @JoinColumn(name = "limit_id")
    private Limits limits;
}
