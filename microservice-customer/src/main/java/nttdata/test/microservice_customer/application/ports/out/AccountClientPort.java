package nttdata.test.microservice_customer.application.ports.out;

import nttdata.test.microservice_customer.domain.models.AccountResponse;
import nttdata.test.microservice_customer.domain.models.CreateAccountCommand;
import nttdata.test.microservice_customer.domain.models.UpdateAccountCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountClientPort {
    Mono<AccountResponse> createAccount(CreateAccountCommand command);

    Mono<AccountResponse> updateAccount(String accountNumber, UpdateAccountCommand command);

    Mono<Void> deleteAccount(String accountNumber);

    Mono<AccountResponse> getByAccountNumber(String accountNumber);

    Flux<AccountResponse> getAccountsByIdentification(String identification);
}
