package gazssha.bank.banktransaction.repository;

import gazssha.bank.banktransaction.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    Optional<BankAccount> findByAccount(Long account);
}
