package nttdata.test.microservice_customer.infrastructure.adapters.in.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import nttdata.test.microservice_customer.application.ports.out.MovementUseCase;
import nttdata.test.microservice_customer.domain.models.CreateMovement;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.mapper.MovementRequestMapper;

@RestController
@RequestMapping("/api/v1/movements/{accountNumber}")
@RequiredArgsConstructor
@Slf4j
public class MovementController {

    private final MovementUseCase movementUseCase;
    private final MovementRequestMapper mapper;

    @PostMapping
    public Mono<ResponseEntity<JsonDtoResponse<Movement>>> createMovement(
            @PathVariable(name = "accountNumber") String accountNumber,
            @Valid @RequestBody CreateMovement request) {
        return movementUseCase.registerMovement(mapper.toCreateCommand(accountNumber, request))
                .map(movement -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(JsonDtoResponse.created("Movement registered", movement)));
    }

    @GetMapping
    public Mono<ResponseEntity<JsonDtoResponse<List<Movement>>>> listMovements(
            @PathVariable(name = "accountNumber") String accountNumber) {
        return movementUseCase.getMovementsByAccountNumber(accountNumber)
                .collectList()
                .map(movements -> ResponseEntity.ok(JsonDtoResponse.ok(
                        "Movements retrieved",
                        movements)));
    }
}
