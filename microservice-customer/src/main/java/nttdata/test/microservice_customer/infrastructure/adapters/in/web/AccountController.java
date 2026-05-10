package nttdata.test.microservice_customer.infrastructure.adapters.in.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

import nttdata.test.microservice_customer.application.ports.out.AccountClientPort;
import nttdata.test.microservice_customer.domain.models.CreateAccountCommand;
import nttdata.test.microservice_customer.domain.models.UpdateAccountCommand;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request.CreateAccount;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request.UpdateAccount;
import nttdata.test.microservice_customer.domain.models.AccountResponse;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.mapper.AccountRequestMapper;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountClientPort accountClientPort;
    private final AccountRequestMapper accountRequestMapper;

    @PostMapping
    public Mono<ResponseEntity<JsonDtoResponse<AccountResponse>>> createAccount(
            @Valid @RequestBody CreateAccount request) {
        CreateAccountCommand command = accountRequestMapper.toCreateCommand(request);
        return accountClientPort.createAccount(command)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(JsonDtoResponse.created("Account created successfully", response)));
    }

    @PutMapping("/{accountNumber}")
    public Mono<ResponseEntity<JsonDtoResponse<AccountResponse>>> updateAccount(
            @PathVariable(name = "accountNumber") String accountNumber,
            @Valid @RequestBody UpdateAccount request) {
        UpdateAccountCommand command = accountRequestMapper.toUpdateCommand(request);
        return accountClientPort.updateAccount(accountNumber, command)
                .map(response -> ResponseEntity.ok(JsonDtoResponse.ok("Account updated", response)));
    }

    @GetMapping("/{accountNumber}")
    public Mono<ResponseEntity<JsonDtoResponse<AccountResponse>>> getByAccountNumber(
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountClientPort.getByAccountNumber(accountNumber)
                .map(response -> ResponseEntity.ok(JsonDtoResponse.ok("Account found", response)));

    }

    @GetMapping("/customer/{identification}")
    public Mono<ResponseEntity<JsonDtoResponse<List<AccountResponse>>>> getByCustomerIdentification(
            @PathVariable(name = "identification") String identification) {
        return accountClientPort.getAccountsByIdentification(identification)
                .collectList()
                .map(list -> ResponseEntity.ok(JsonDtoResponse.ok("Accounts found", list)));
    }

    @DeleteMapping("/{accountNumber}")
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> deleteAccount(
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountClientPort.deleteAccount(accountNumber)
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(JsonDtoResponse.ok("Account deleted")));
    }

}
