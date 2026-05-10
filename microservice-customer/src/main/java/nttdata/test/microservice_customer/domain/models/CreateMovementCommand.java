package nttdata.test.microservice_customer.domain.models;

import java.math.BigDecimal;

public record CreateMovementCommand(
        String accountNumber,
        BigDecimal amount,
        String type,
        String description) {
}
