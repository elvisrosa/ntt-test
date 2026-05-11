package nttdata.test.microservice_customer.application.ports.out;

import nttdata.test.microservice_customer.domain.models.CreateMovementCommand;
import nttdata.test.microservice_customer.domain.models.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementUseCase {

    Mono<Movement> registerMovement(CreateMovementCommand command);

    Flux<Movement> getMovementsByAccountNumber(String accountNumber);
    
    Flux<Movement> getMovementsByClientIdentificationAndDateRange(String clientIdentification, 
            String startDate, String endDate);

}
