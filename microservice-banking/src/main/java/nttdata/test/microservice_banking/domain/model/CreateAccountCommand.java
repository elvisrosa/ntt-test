package nttdata.test.microservice_banking.domain.model;

import java.math.BigDecimal;

public record CreateAccountCommand(
        String accountNumber,
        String accountType,
        BigDecimal initialBalance,
        BigDecimal currentBalance,
        String clientIdentification) {
}