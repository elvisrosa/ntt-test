package nttdata.test.microservice_customer.infrastructure.adapters.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_customer.application.ports.out.MovementClientPort;
import nttdata.test.microservice_customer.domain.models.CreateMovementCommand;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.infrastructure.adapters.out.mapper.WebClientErrorUtils;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientMovementAdapter implements MovementClientPort {

    private final WebClient bankingWebClient;

    @Override
    public Mono<Movement> registerMovement(CreateMovementCommand command) {
        log.debug("[WEB-CLIENT] POST {} /api/v1/movements/" + command.accountNumber());
        return bankingWebClient.post()
                .uri("/movements/{accountNumber}", command.accountNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<Movement>>() {
                        }))
                .map(dto -> dto.getResult());
    }

    @Override
    public Flux<Movement> getMovementsByAccountNumber(String accountNumber) {
        return bankingWebClient.get()
                .uri("/movements/{accountNumber}", accountNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> WebClientErrorUtils.handleResponse(response,
                        new ParameterizedTypeReference<JsonDtoResponse<List<Movement>>>() {
                        }))
                .map(JsonDtoResponse::getResult)
                .flatMapMany(Flux::fromIterable);
    }

}
