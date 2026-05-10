package nttdata.test.microservice_banking.domain.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;

public class Movement {

    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private String type;
    private BigDecimal balanceAfter;
    private String description;
    private LocalDateTime createdAt;

    public Movement() {
    }

    public Movement(UUID accountId, BigDecimal amount, String type, BigDecimal balanceAfter, String description,
            LocalDateTime createdAt) {
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Movement(UUID id, UUID accountId, BigDecimal amount, String type, BigDecimal balanceAfter,
            String description,
            LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public java.math.BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(java.math.BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
