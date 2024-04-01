package gazssha.bank.banktransaction.exception;

public class NotFoundBankAccount extends RuntimeException {
    public NotFoundBankAccount(String message) {
        super(message);
    }
}
