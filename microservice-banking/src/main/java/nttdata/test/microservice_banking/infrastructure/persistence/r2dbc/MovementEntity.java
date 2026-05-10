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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("movement")
public class MovementEntity {

    @Id
    private UUID id;

    @NotNull(message = "Account ID cannot be null")
    @Column("account_id")
    private UUID accountId;

    @NotNull(message = "Movement type cannot be null")
    @Column("movement_type")
    private String movementType;

    @Column("transaction_date")
    private LocalDateTime transactionDate;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column("amount")
    private BigDecimal amount;

    @Column("balance_after")
    private BigDecimal balanceAfter;

    @Column
    private String description;

    @Column("created_at")
    private LocalDateTime createdAt;

}
