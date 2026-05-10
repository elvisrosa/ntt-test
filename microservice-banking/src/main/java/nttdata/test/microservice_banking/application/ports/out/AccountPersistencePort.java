package nttdata.test.microservice_banking.application.ports.out;

import nttdata.test.microservice_banking.domain.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountPersistencePort {

    Mono<Account> saveAccount(Account account);

    Mono<Account> updateAccount(Account account);

    Mono<Account> findByAccountNumber(String accountNumber);

    Flux<Account> findByClientIdentification(String identification);

    Mono<Void> deleteAccountById(UUID id);

    Mono<UUID> findClientIdByIdentification(String identification);

    Mono<Account> findById(UUID id);

}
