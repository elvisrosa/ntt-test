package nttdata.test.microservice_banking.infrastructure.adapters.out;

import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.MovementEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import java.util.UUID;

public interface MovementRepository extends ReactiveCrudRepository<MovementEntity, UUID> {

    @Query("SELECT * FROM movement WHERE account_id = :accountId")
    Flux<MovementEntity> findByAccountId(UUID accountId);

}
