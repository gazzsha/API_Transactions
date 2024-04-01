package gazssha.client.clienttransaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_account")
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "account", unique = true, nullable = false, updatable = false)
    private Long account;

    @NotNull
    @Column(name = "money_service", nullable = false)
    private BigDecimal moneyService;

    @NotNull
    @Column(name = "money_product", nullable = false)
    private BigDecimal moneyProduct;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "bankAccount")
    private List<Limits> limits = new ArrayList<>();

    public void addLimit(Limits limit) {
        limits.add(limit);
        limit.setBankAccount(this);
    }
}
