package nttdata.test.microservice_customer.domain.models;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String accountNumber,
        String accountType,
        BigDecimal balance) {
}
