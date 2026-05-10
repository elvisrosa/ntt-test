package nttdata.test.microservice_banking.infrastructure.adapters.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_banking.application.ports.out.AccountUseCase;
import nttdata.test.microservice_banking.domain.model.AccountResponse;
import nttdata.test.microservice_banking.domain.model.CreateAccountCommand;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request.CreateAccount;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request.UpdateAccount;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.mapper.AccountRequestMapper;
import reactor.core.publisher.Mono;
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountRequestMapper accountRequestMapper;

    @PostMapping
    public Mono<ResponseEntity<JsonDtoResponse<AccountResponse>>> createAccount(
            @Valid @RequestBody CreateAccount request) {
        CreateAccountCommand command = accountRequestMapper.toCreateCommand(request);
        return accountUseCase.createAccount(command)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(JsonDtoResponse.created("Account created successfully",
                                response)));
    }

    @PutMapping("/{accountNumber}")
    public Mono<ResponseEntity<JsonDtoResponse<AccountResponse>>> updateAccount(
            @PathVariable(name = "accountNumber") String accountNumber,
            @Valid @RequestBody UpdateAccount request) {
        return accountUseCase.updateAccount(accountNumber, accountRequestMapper.toUpdateCommand(request))
                .map(response -> ResponseEntity
                        .ok(JsonDtoResponse.ok("Account updated", response)));
    }

    @GetMapping("/{accountNumber}")
    public Mono<ResponseEntity<JsonDtoResponse<AccountResponse>>> getByAccountNumber(
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountUseCase.getAccountByNumber(accountNumber)
                .map(response -> ResponseEntity
                        .ok(JsonDtoResponse.ok("Account found", response)));

    }

    @GetMapping("/customer/{identification}")
    public Mono<ResponseEntity<JsonDtoResponse<List<AccountResponse>>>> getByCustomerIdentification(
            @PathVariable(name = "identification") String identification) {
        return accountUseCase.getAccountsByIdentification(identification)
                .collectList()
                .map(list -> ResponseEntity.ok(JsonDtoResponse.ok("Accounts found", list)));
    }

    @DeleteMapping("/{accountNumber}")
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> deleteAccount(
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountUseCase.deleteAccountByAccountNumber(accountNumber)
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(JsonDtoResponse.ok("Account deleted")));
    }

}
