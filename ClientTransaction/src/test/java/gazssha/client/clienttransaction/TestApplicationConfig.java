package gazssha.client.clienttransaction;


import gazssha.client.clienttransaction.repository.BankAccountRepository;
import gazssha.client.clienttransaction.repository.TransactionRepository;
import gazssha.client.clienttransaction.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TestApplicationConfig {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Bean
    public BankService bankService() {
        return new BankService(bankAccountRepository,transactionRepository);
    }

}
