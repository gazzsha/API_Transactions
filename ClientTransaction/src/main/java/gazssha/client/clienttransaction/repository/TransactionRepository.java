package gazssha.client.clienttransaction.repository;

import gazssha.client.clienttransaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>
{
    @Query("SELECT t from Transaction t join t.limits l where t.limitExceeded is true" )
    List<Transaction> findAllByLimits();
}
