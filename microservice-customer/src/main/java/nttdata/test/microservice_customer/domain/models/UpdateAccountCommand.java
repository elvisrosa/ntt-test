package nttdata.test.microservice_customer.domain.models;

import java.math.BigDecimal;

public record UpdateAccountCommand(
        String customerIdentification,
        BigDecimal initialBalance) {
}
