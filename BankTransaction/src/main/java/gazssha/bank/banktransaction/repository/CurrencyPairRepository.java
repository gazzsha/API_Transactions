package gazssha.bank.banktransaction.repository;

import gazssha.bank.banktransaction.entity.CurrencyPair;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface CurrencyPairRepository extends CassandraRepository<CurrencyPair, UUID> {
    CurrencyPair findCurrencyPairByShortSymbol(String symbol);
}

