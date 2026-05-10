package nttdata.test.microservice_banking.application.service;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import nttdata.test.microservice_banking.application.ports.out.AccountPersistencePort;
import nttdata.test.microservice_banking.application.ports.out.MovementPersistencePort;
import nttdata.test.microservice_banking.domain.exception.ExceptionInsufficientFunds;
import nttdata.test.microservice_banking.domain.model.Account;
import nttdata.test.microservice_banking.domain.model.CreateMovementCommand;
import nttdata.test.microservice_banking.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    AccountPersistencePort accountPersistencePort;

    @Mock
    MovementPersistencePort movementPersistencePort;

    @InjectMocks
    MovementService movementService;

    @Captor
    ArgumentCaptor<Movement> movementCaptor;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(UUID.randomUUID());
        account.setAccountNumber("ACC-123");
        account.setCurrentBalance(new BigDecimal("100.00"));
    }

    @Test
    void registerMovement_credit_updatesBalanceAndSavesMovement() {
        CreateMovementCommand cmd = new CreateMovementCommand("ACC-123", new BigDecimal("25.00"), "CREDIT", "salary");

        Account updated = new Account(account.getId(), account.getAccountNumber(), account.getClientId(), account.getAccountType(), account.getStatus(), account.getInitialBalance(), new BigDecimal("125.00"), account.getCreatedAt());

        when(accountPersistencePort.findByAccountNumber("ACC-123")).thenReturn(Mono.just(account));
        when(accountPersistencePort.updateAccount(any())).thenReturn(Mono.just(updated));
        when(movementPersistencePort.saveMovement(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Movement> result = movementService.registerMovement(cmd);

        StepVerifier.create(result)
                .assertNext(m -> {
                    assertEquals(new BigDecimal("125.00"), m.getBalanceAfter());
                    assertEquals("Credito", m.getType());
                    assertEquals(cmd.amount(), m.getAmount());
                })
                .verifyComplete();

        verify(accountPersistencePort).updateAccount(any());
        verify(movementPersistencePort).saveMovement(movementCaptor.capture());
    }

    @Test
    void registerMovement_debit_withSufficientFunds_updatesBalance() {
        CreateMovementCommand cmd = new CreateMovementCommand("ACC-123", new BigDecimal("40.00"), "DEBIT", "purchase");

        Account updated = new Account(account.getId(), account.getAccountNumber(), account.getClientId(), account.getAccountType(), account.getStatus(), account.getInitialBalance(), new BigDecimal("60.00"), account.getCreatedAt());

        when(accountPersistencePort.findByAccountNumber("ACC-123")).thenReturn(Mono.just(account));
        when(accountPersistencePort.updateAccount(any())).thenReturn(Mono.just(updated));
        when(movementPersistencePort.saveMovement(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(movementService.registerMovement(cmd))
                .assertNext(m -> {
                    assertEquals(new BigDecimal("60.00"), m.getBalanceAfter());
                    assertEquals("Debito", m.getType());
                })
                .verifyComplete();
    }

    @Test
    void registerMovement_debit_insufficientFunds_throws() {
        CreateMovementCommand cmd = new CreateMovementCommand("ACC-123", new BigDecimal("200.00"), "DEBIT", "big purchase");

        when(accountPersistencePort.findByAccountNumber("ACC-123")).thenReturn(Mono.just(account));

        StepVerifier.create(movementService.registerMovement(cmd))
                .expectErrorSatisfies(err -> assertTrue(err instanceof ExceptionInsufficientFunds))
                .verify();
    }

    @Test
    void registerMovement_invalidAmount_throws() {
        CreateMovementCommand cmd = new CreateMovementCommand("ACC-123", new BigDecimal("0"), "CREDIT", "invalid");

        StepVerifier.create(movementService.registerMovement(cmd))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("El valor del movimiento"))
                .verify();
    }

    @Test
    void getMovementsByAccountNumber_returnsMovements() {
        Movement m1 = new Movement(account.getId(), new BigDecimal("10.00"), "Credito", new BigDecimal("110.00"), "a", null);
        Movement m2 = new Movement(account.getId(), new BigDecimal("5.00"), "Debito", new BigDecimal("105.00"), "b", null);

        when(accountPersistencePort.findByAccountNumber("ACC-123")).thenReturn(Mono.just(account));
        when(movementPersistencePort.findByAccountId(account.getId())).thenReturn(Flux.just(m1, m2));

        StepVerifier.create(movementService.getMovementsByAccountNumber("ACC-123"))
                .expectNext(m1)
                .expectNext(m2)
                .verifyComplete();
    }

    @Test
    void registerMovement_accountNotFound_throws() {
        CreateMovementCommand cmd = new CreateMovementCommand("NON-EXISTENT", new BigDecimal("25.00"), "CREDIT", "test");

        when(accountPersistencePort.findByAccountNumber("NON-EXISTENT")).thenReturn(Mono.empty());

        StepVerifier.create(movementService.registerMovement(cmd))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Account not found"))
                .verify();
    }

    @Test
    void getMovementsByAccountNumber_accountNotFound_throws() {
        when(accountPersistencePort.findByAccountNumber("NON-EXISTENT")).thenReturn(Mono.empty());

        StepVerifier.create(movementService.getMovementsByAccountNumber("NON-EXISTENT"))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Account not found"))
                .verify();
    }

    @Test
    void registerMovement_negativeAmount_throws() {
        CreateMovementCommand cmd = new CreateMovementCommand("ACC-123", new BigDecimal("-50.00"), "CREDIT", "invalid");

        StepVerifier.create(movementService.registerMovement(cmd))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("El valor del movimiento"))
                .verify();
    }

}
