package gazssha.bank.banktransaction.exception;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {}