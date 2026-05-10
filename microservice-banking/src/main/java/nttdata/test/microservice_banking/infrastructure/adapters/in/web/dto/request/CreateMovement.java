package nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateMovement(
        @NotNull(message = "Amount is required") @Positive(message = "Amount must be greater than 0") BigDecimal amount,
        @NotNull(message = "Type is required") String type,
        String description) {
}
