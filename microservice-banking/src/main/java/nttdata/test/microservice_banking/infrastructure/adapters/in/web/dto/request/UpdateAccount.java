package nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UpdateAccount(
        String accountType,
        @NotBlank(message = "Current balance is required") @PositiveOrZero(message = "Current balance must be greater than or equal to 0") BigDecimal currentBalance) {
}
