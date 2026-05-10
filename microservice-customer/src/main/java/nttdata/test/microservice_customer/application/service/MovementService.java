package nttdata.test.microservice_customer.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_customer.application.ports.out.MovementClientPort;
import nttdata.test.microservice_customer.application.ports.out.MovementUseCase;
import nttdata.test.microservice_customer.domain.models.CreateMovementCommand;
import nttdata.test.microservice_customer.domain.models.Movement;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementService implements MovementUseCase {

    private final MovementClientPort movementClientPort;

    @Override
    public Mono<Movement> registerMovement(CreateMovementCommand command) {
        log.debug("[MOVEMENT-SERVICE] register movement for accountNumber={}", command.accountNumber());
        return movementClientPort.registerMovement(command);
    }

    @Override
    public Flux<Movement> getMovementsByAccountNumber(String accountNumber) {
        log.debug("[MOVEMENT-SERVICE] get movements for accountNumber={}", accountNumber);
        return movementClientPort.getMovementsByAccountNumber(accountNumber);
    }

}
