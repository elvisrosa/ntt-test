package nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request;

import java.math.BigDecimal;

public record CreateAccount(
        String accountNumber,
        String accountType,
        String customerIdentification,
        BigDecimal initialBalance) {
}
