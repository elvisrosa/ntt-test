package nttdata.test.microservice_banking.infrastructure.adapters.out;

import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.AccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.UUID;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, UUID> {

    @Query("SELECT * FROM account WHERE account_number = :accountNumber AND status = true")
    Mono<AccountEntity> findByAccountNumber(String accountNumber);

    @Query("SELECT * FROM account WHERE client_id = :clientId AND status = true")
    Flux<AccountEntity> findByClientId(UUID clientId);

}
