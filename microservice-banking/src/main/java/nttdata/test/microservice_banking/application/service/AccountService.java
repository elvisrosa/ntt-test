package nttdata.test.microservice_banking.application.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_banking.application.ports.out.AccountPersistencePort;
import nttdata.test.microservice_banking.application.ports.out.AccountUseCase;
import nttdata.test.microservice_banking.domain.exception.ExceptionCustom;
import nttdata.test.microservice_banking.domain.model.Account;
import nttdata.test.microservice_banking.domain.model.AccountType;
import nttdata.test.microservice_banking.domain.model.AccountResponse;
import nttdata.test.microservice_banking.domain.model.CreateAccountCommand;
import nttdata.test.microservice_banking.domain.model.UpdateAccountCommand;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService implements AccountUseCase {

        private final AccountPersistencePort accountPersistencePort;
        private final String ACCOUNT_NOT_FOUND = "Account not found";

        @Override
        public Mono<AccountResponse> createAccount(CreateAccountCommand command) {
                AccountType validatedType = AccountType.from(command.accountType());
                return accountPersistencePort.findClientIdByIdentification(command.clientIdentification())
                                .switchIfEmpty(Mono.error(
                                                new ExceptionCustom("Client not found", HttpStatus.NOT_FOUND.value())))
                                .flatMap(clientId -> accountPersistencePort.findByAccountNumber(command.accountNumber())
                                                .flatMap(existing -> Mono.<Account>error(
                                                                new ExceptionCustom("Account number already exists",
                                                                                HttpStatus.CONFLICT.value())))
                                                .switchIfEmpty(Mono.defer(() -> {
                                                        Account account = new Account(
                                                                        command.accountNumber(),
                                                                        clientId,
                                                                        validatedType.getDescription(),
                                                                        Boolean.TRUE,
                                                                        command.initialBalance(),
                                                                        command.currentBalance());
                                                        return accountPersistencePort.saveAccount(account);
                                                })))
                                .map(a -> new AccountResponse(a.getId(), a.getAccountNumber(), a.getAccountType(),
                                                a.getCurrentBalance()));
        }

        @Override
        public Mono<AccountResponse> updateAccount(String accountNumber, UpdateAccountCommand command) {
                return accountPersistencePort.findByAccountNumber(accountNumber)
                                .switchIfEmpty(Mono.error(new ExceptionCustom(ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.value())))
                                .flatMap(existing -> {
                                        if (command.accountType() != null) {
                                                AccountType validatedType = AccountType.from(command.accountType());
                                                existing.setAccountType(validatedType.getDescription());
                                        }
                                        existing.setCurrentBalance(command.currentBalance());
                                        return accountPersistencePort.updateAccount(existing);
                                })
                                .map(a -> new AccountResponse(a.getId(), a.getAccountNumber(), a.getAccountType(),
                                                a.getCurrentBalance()));
        }

        @Override
        public Mono<Void> deleteAccountByAccountNumber(String accountNumber) {
                return accountPersistencePort.findByAccountNumber(accountNumber)
                                .switchIfEmpty(Mono.error(new ExceptionCustom(ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.value())))
                                .flatMap(existing -> {
                                        String accountRamdon = existing.getAccountNumber() + "_d" + UUID.randomUUID();
                                        String accountTrim30 = accountRamdon.length() > 30
                                                        ? accountRamdon.substring(0, 30)
                                                        : accountRamdon;
                                        existing.setStatus(Boolean.FALSE);
                                        existing.setAccountNumber(accountTrim30);
                                        return accountPersistencePort.updateAccount(existing);
                                })
                                .then();
        }

        @Override
        public Mono<AccountResponse> getAccountByNumber(String accountNumber) {
                return accountPersistencePort.findByAccountNumber(accountNumber)
                                .switchIfEmpty(Mono.error(new ExceptionCustom(ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.value())))
                                .map(a -> new AccountResponse(a.getId(), a.getAccountNumber(), a.getAccountType(),
                                                a.getCurrentBalance()));
        }

        @Override
        public Flux<AccountResponse> getAccountsByIdentification(String identification) {
                return accountPersistencePort.findByClientIdentification(identification)
                                .switchIfEmpty(
                                                Mono.error(new ExceptionCustom(ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.value())))
                                .map(a -> new AccountResponse(a.getId(), a.getAccountNumber(), a.getAccountType(),
                                                a.getCurrentBalance()));
        }

}
