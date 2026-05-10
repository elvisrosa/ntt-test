package nttdata.test.microservice_banking.infrastructure.adapters.out.mapper;

import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.AccountEntity;
import nttdata.test.microservice_banking.domain.model.Account;
import nttdata.test.microservice_banking.domain.model.AccountResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j

@Component
public class AccountEntityMapper {

    public Account toDomain(AccountEntity entity) {
        if (entity == null)
            return null;
        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getClientId(),
                entity.getAccountType(),
                entity.getStatus(),
                entity.getInitialBalance(),
                entity.getCurrentBalance(),
                entity.getCreatedAt());
    }

    public AccountEntity toEntity(Account account) {
        if (account == null)
            return null;
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .clientId(account.getClientId())
                .accountType(account.getAccountType())
                .status(account.getStatus() == null ? Boolean.TRUE : account.getStatus())
                .createdAt(account.getCreatedAt() == null ? LocalDateTime.now() : account.getCreatedAt())
                .initialBalance(account.getInitialBalance() == null ? BigDecimal.ZERO : account.getInitialBalance())
                .currentBalance(account.getCurrentBalance() == null ? BigDecimal.ZERO : account.getCurrentBalance())
                .updatedAt(account.getUpdatedAt() == null ? LocalDateTime.now() : account.getUpdatedAt())
                .build();
    }

    public AccountResponse toResponse(Account account) {
        if (account == null)
            return null;
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCurrentBalance());
    }

}
