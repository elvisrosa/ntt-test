package nttdata.test.microservice_banking.application.ports.out;

import java.util.UUID;

import nttdata.test.microservice_banking.domain.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementPersistencePort {

    Mono<Movement> saveMovement(Movement movement);

    Flux<Movement> findByAccountId(UUID accountId);

    Flux<Movement> findByClientIdentificationAndDateRange(String identification, java.time.LocalDateTime start, java.time.LocalDateTime end);

}
