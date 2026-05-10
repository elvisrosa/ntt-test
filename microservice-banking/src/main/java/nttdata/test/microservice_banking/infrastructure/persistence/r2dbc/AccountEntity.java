package nttdata.test.microservice_banking.infrastructure.persistence.r2dbc;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("account")
public class AccountEntity {

    @Id
    private UUID id;

    @NotBlank(message = "Account number cannot be blank")
    @Size(min = 10, max = 30, message = "Account number must be between 10 and 30 characters")
    @Column("account_number")
    private String accountNumber;

    @NotNull(message = "Client ID cannot be null")
    @Column("client_id")
    private UUID clientId;

    @NotNull(message = "Account type cannot be null")
    @Column("account_type")
    private String accountType;

    @NotNull(message = "Status cannot be null")
    @Column("status")
    private Boolean status;

    @NotNull(message = "Initial balance cannot be null")
    @DecimalMin(value = "0.0", message = "Initial balance must be >= 0")
    @Column("initial_balance")
    private BigDecimal initialBalance;

    @NotNull(message = "Current balance cannot be null")
    @Column("current_balance")
    private BigDecimal currentBalance;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

}
