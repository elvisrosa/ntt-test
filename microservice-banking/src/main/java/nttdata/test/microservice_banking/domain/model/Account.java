package nttdata.test.microservice_banking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {

    private UUID id;

    private String accountNumber;

    private UUID clientId;

    private String accountType;

    private Boolean status;

    private BigDecimal initialBalance;

    private BigDecimal currentBalance;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Account() {
    }

    public Account(String accountNumber, UUID clientId, String accountType, Boolean status,
            BigDecimal initialBalance, BigDecimal currentBalance) {
        this.accountNumber = accountNumber;
        this.clientId = clientId;
        this.accountType = accountType;
        this.status = status;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
    }

    public Account(UUID id, String accountNumber, UUID clientId, String accountType, Boolean status,
            BigDecimal initialBalance, BigDecimal currentBalance, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.clientId = clientId;
        this.accountType = accountType;
        this.status = status;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
