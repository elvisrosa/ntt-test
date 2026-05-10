package nttdata.test.microservice_banking.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String accountNumber,
        String accountType,
        BigDecimal balance) {
}
