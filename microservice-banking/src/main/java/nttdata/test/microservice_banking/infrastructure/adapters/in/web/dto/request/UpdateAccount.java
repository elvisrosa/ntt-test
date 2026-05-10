package nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request;

import java.math.BigDecimal;

public record UpdateAccount(
                String customerIdentification,
                BigDecimal initialBalance) {
}
