package gazssha.client.clienttransaction.exception;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {}