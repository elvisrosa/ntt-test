package nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request;

import java.math.BigDecimal;

public record CreateMovement(
        BigDecimal amount,
        String type,
        String description) {
}
