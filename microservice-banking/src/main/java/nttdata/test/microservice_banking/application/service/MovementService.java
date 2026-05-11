package nttdata.test.microservice_banking.application.service;

import nttdata.test.microservice_banking.application.ports.out.MovementPersistencePort;
import nttdata.test.microservice_banking.application.ports.out.MovementUseCase;
import nttdata.test.microservice_banking.application.ports.out.AccountPersistencePort;
import nttdata.test.microservice_banking.domain.exception.ExceptionCustom;
import nttdata.test.microservice_banking.domain.exception.ExceptionInsufficientFunds;
import nttdata.test.microservice_banking.domain.model.CreateMovementCommand;
import nttdata.test.microservice_banking.domain.model.Movement;
import nttdata.test.microservice_banking.domain.model.MovementType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementService implements MovementUseCase {

    private final AccountPersistencePort accountPersistencePort;
    private final MovementPersistencePort movementPersistencePort;

    @Override
    public Mono<Movement> registerMovement(CreateMovementCommand command) {
        if (command.amount() == null || command.amount().doubleValue() <= 0) {
            return Mono.error(new ExceptionCustom("El valor del movimiento debe ser mayor que cero",
                    HttpStatus.BAD_REQUEST.value()));
        }

        MovementType type = MovementType.from(command.type());

        return accountPersistencePort.findByAccountNumber(command.accountNumber())
                .switchIfEmpty(Mono.error(new ExceptionCustom("Account not found", HttpStatus.NOT_FOUND.value())))
                .flatMap(account -> {
                    if (type == MovementType.DEBIT) {
                        if (account.getCurrentBalance() == null
                                || account.getCurrentBalance().doubleValue() < command.amount().doubleValue()) {
                            return Mono.error(new ExceptionInsufficientFunds());
                        }
                        account.setCurrentBalance(account.getCurrentBalance().subtract(command.amount()));
                    } else {
                        account.setCurrentBalance(account.getCurrentBalance().add(command.amount()));
                    }

                    return accountPersistencePort.updateAccount(account)
                            .flatMap(updated -> {
                                Movement movement = new Movement(updated.getId(), command.amount(),
                                        type.getDescription(),
                                        updated.getCurrentBalance(), command.description(), LocalDateTime.now());
                                movement.setAccountId(updated.getId());
                                return movementPersistencePort.saveMovement(movement);
                            });
                });
    }

    @Override
    public Flux<Movement> getMovementsByAccountNumber(String accountNumber) {
        return accountPersistencePort.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ExceptionCustom("Account not found", HttpStatus.NOT_FOUND.value())))
                .flatMapMany(account -> movementPersistencePort.findByAccountId(account.getId()));
    }

    @Override
    public Flux<Movement> getMovementsByClientIdentificationAndDateRange(String identification, LocalDateTime start, LocalDateTime end) {
        return movementPersistencePort.findByClientIdentificationAndDateRange(identification, start, end);
    }

}
