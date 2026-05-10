package nttdata.test.microservice_banking.domain.model;

import java.math.BigDecimal;

public record UpdateAccountCommand(
        String accountType,
        BigDecimal currentBalance) {
}
