package gazssha.bank.banktransaction.repository;


import gazssha.bank.banktransaction.entity.Limits;
import gazssha.bank.banktransaction.utils.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitRepository extends JpaRepository<Limits,Long> {

    Limits findTopByBankAccountIdAndCategoryOrderByDateTime(Long bankAccount, Category category);
}
