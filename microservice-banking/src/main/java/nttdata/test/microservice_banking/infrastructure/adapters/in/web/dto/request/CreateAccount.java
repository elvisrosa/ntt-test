package nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAccount(
                @NotBlank(message = "Account number is required") String accountNumber,
                @NotBlank(message = "Account type is required") String accountType,
                @NotBlank(message = "Customer identification is required") String customerIdentification,
                @NotNull(message = "Initial balance is required") @Positive(message = "Initial balance must be greater than 0") BigDecimal initialBalance) {
}
