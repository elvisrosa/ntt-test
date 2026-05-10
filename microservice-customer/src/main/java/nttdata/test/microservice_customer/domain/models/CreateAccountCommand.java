package nttdata.test.microservice_customer.domain.models;

import java.math.BigDecimal;

public record CreateAccountCommand(
        String accountNumber,
        String accountType,
        BigDecimal initialBalance,
        BigDecimal currentBalance,
        String customerIdentification) {
}
