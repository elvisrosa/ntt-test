package nttdata.test.microservice_customer.application.ports.out;

import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.domain.models.CreateMovementCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementClientPort {

    Mono<Movement> registerMovement(CreateMovementCommand command);

    Flux<Movement> getMovementsByAccountNumber(String accountNumber);
    
    Flux<Movement> getMovementsByClientIdentificationAndDateRange(String clientIdentification, 
            String startDate, String endDate);

}
