package gazssha.bank.banktransaction.entity;


import gazssha.bank.banktransaction.utils.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "limits")
@Getter
@Setter
public class Limits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "limit_sum", nullable = false)
    private BigDecimal limitSum;
    @NotNull
    @Column(name = "category", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(name = "date_time", nullable = false)
    @Basic
    private ZonedDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private BankAccount bankAccount;
}
