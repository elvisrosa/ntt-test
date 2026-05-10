package nttdata.test.microservice_banking.domain.model;

import java.math.BigDecimal;

public record CreateMovementCommand(
        String accountNumber,
        BigDecimal amount,
        String type,
        String description) {
}
