package nttdata.test.microservice_banking.application.ports.out;

import nttdata.test.microservice_banking.domain.model.AccountResponse;
import nttdata.test.microservice_banking.domain.model.CreateAccountCommand;
import nttdata.test.microservice_banking.domain.model.UpdateAccountCommand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountUseCase {

    Mono<AccountResponse> createAccount(CreateAccountCommand command);

    Mono<AccountResponse> updateAccount(String accountNumber, UpdateAccountCommand command);

    Mono<Void> deleteAccountByAccountNumber(String accountNumber);

    Mono<AccountResponse> getAccountByNumber(String accountNumber);

    Flux<AccountResponse> getAccountsByIdentification(String identification);

}
