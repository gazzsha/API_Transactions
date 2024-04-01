package gazssha.client.clienttransaction.exception;

public class NotFoundBankAccount extends RuntimeException {
    public NotFoundBankAccount(String message) {
        super(message);
    }
}
