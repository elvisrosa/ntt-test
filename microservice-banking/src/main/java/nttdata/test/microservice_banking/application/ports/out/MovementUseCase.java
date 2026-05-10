package nttdata.test.microservice_banking.application.ports.out;

import nttdata.test.microservice_banking.domain.model.CreateMovementCommand;
import nttdata.test.microservice_banking.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementUseCase {

    Mono<Movement> registerMovement(CreateMovementCommand command);

    Flux<Movement> getMovementsByAccountNumber(String accountNumber);

}
