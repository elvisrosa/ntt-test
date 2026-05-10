package nttdata.test.microservice_customer.infrastructure.adapters.out;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import nttdata.test.microservice_customer.infrastructure.persistence.r2dbc.ClientEntity;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<ClientEntity, UUID> {

    @Query("SELECT * FROM client WHERE identification = :identification AND status = true")
    Mono<ClientEntity> findByIdentification(String identification);

    @Query("SELECT * FROM client WHERE identification = :identification AND password = :password AND status = true")
    Mono<ClientEntity> findByIdentificationAndPassword(String identification, String password);
}
