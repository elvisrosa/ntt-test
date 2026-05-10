package nttdata.test.microservice_customer.infrastructure.adapters.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_customer.application.ports.out.AccountClientPort;
import nttdata.test.microservice_customer.domain.models.AccountResponse;
import nttdata.test.microservice_customer.domain.models.CreateAccountCommand;
import nttdata.test.microservice_customer.domain.models.UpdateAccountCommand;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.infrastructure.adapters.out.mapper.WebClientErrorUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientAccountAdapter implements AccountClientPort {

    private final WebClient bankingWebClient;
    private static final String BASE_URI = "/accounts";
    private static final String ACCOUNT_URL = BASE_URI + "/{accountNumber}";

    @Override
    public Mono<AccountResponse> createAccount(CreateAccountCommand command) {
        log.debug("[WEB-CLIENT] POST /api/v1/accounts");
        return bankingWebClient.post()
                .uri(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<AccountResponse>>() {
                        }))
                .map(JsonDtoResponse::getResult);
    }

    @Override
    public Mono<AccountResponse> updateAccount(String accountNumber, UpdateAccountCommand command) {
        log.debug("[WEB-CLIENT] PUT /api/v1/accounts/{}", accountNumber);
        return bankingWebClient.put()
                .uri(ACCOUNT_URL, accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<AccountResponse>>() {
                        }))
                .map(JsonDtoResponse::getResult);
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        log.debug("[WEB-CLIENT] DELETE /api/v1/accounts/{}", accountNumber);
        return bankingWebClient.delete()
                .uri(ACCOUNT_URL, accountNumber)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<Object>>() {
                        }))
                .then();
    }

    @Override
    public Mono<AccountResponse> getByAccountNumber(String accountNumber) {
        log.debug("[WEB-CLIENT] GET /api/v1/accounts/{}", accountNumber);
        return bankingWebClient.get()
                .uri(ACCOUNT_URL, accountNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<AccountResponse>>() {
                        }))
                .map(JsonDtoResponse::getResult);
    }

    @Override
    public Flux<AccountResponse> getAccountsByIdentification(String identification) {
        log.debug("[WEB-CLIENT] GET /api/v1/accounts/customer/{}", identification);
        return bankingWebClient.get()
                .uri(BASE_URI + "/customer/{identification}", identification)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<java.util.List<AccountResponse>>>() {
                        }))
                .map(JsonDtoResponse::getResult)
                .flatMapMany(Flux::fromIterable);
    }

}
