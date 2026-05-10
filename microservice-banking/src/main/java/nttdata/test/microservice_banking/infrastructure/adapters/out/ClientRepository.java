package nttdata.test.microservice_banking.infrastructure.adapters.out;

import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.ClientEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface ClientRepository extends ReactiveCrudRepository<ClientEntity, UUID> {

    @Query("SELECT * FROM client WHERE identification = :identification AND status = true")
    Mono<ClientEntity> findByIdentification(String identification);

}
