package nttdata.test.microservice_customer.domain.models;

import java.math.BigDecimal;

public record CreateMovement(
        BigDecimal amount,
        String type,
        String description) {
}
