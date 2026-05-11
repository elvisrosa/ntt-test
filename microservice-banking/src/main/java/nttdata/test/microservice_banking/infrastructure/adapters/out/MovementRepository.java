package nttdata.test.microservice_banking.infrastructure.adapters.out;

import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.MovementEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;
import java.util.UUID;

public interface MovementRepository extends ReactiveCrudRepository<MovementEntity, UUID> {

    @Query("SELECT * FROM movement WHERE account_id = :accountId")
    Flux<MovementEntity> findByAccountId(UUID accountId);

    @Query("SELECT m.* FROM movement m JOIN account a ON m.account_id = a.id JOIN client c ON a.client_id = c.id WHERE c.identification = :identification AND m.created_at >= :start AND m.created_at <= :end")
    Flux<MovementEntity> findByClientIdentificationAndDateRange(String identification, LocalDateTime start, LocalDateTime end);

}
