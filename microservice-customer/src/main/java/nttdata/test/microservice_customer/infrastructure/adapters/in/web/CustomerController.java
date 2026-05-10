package nttdata.test.microservice_customer.infrastructure.adapters.in.web;

import nttdata.test.microservice_customer.infrastructure.adapters.in.web.mapper.CustomerRequestMapper;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request.CreateCustomer;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request.UpdateCustomer;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.application.ports.out.CustomerUseCase;
import nttdata.test.microservice_customer.domain.models.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerUseCase customerUseCase;
    private final CustomerRequestMapper requestMapper;

    @PostMapping
    public Mono<ResponseEntity<JsonDtoResponse<CustomerResponse>>> createCustomer(
            @Valid @RequestBody CreateCustomer request) {
        CreateCustomerCommand command = requestMapper.toCreateCommand(request);
        return customerUseCase.createCustomer(command)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(JsonDtoResponse.created("Customer created successfully", response)));
    }

    @PutMapping("/{identification}")
    public Mono<ResponseEntity<JsonDtoResponse<CustomerResponse>>> updateCustomerByIdentification(
            @PathVariable(name = "identification") String identification,
            @Valid @RequestBody UpdateCustomer request) {
        UpdateCustomerCommand command = requestMapper.toUpdateCommand(request);
        return customerUseCase.updateCustomerByIdentification(identification, command)
                .map(response -> ResponseEntity.ok(JsonDtoResponse.ok("Customer updated successfully", response)));
    }

    @GetMapping("/{identification}")
    public Mono<ResponseEntity<JsonDtoResponse<CustomerResponse>>> getCustomerByIdentification(
            @PathVariable(name = "identification") String identification) {
        log.info("GET /api/v1/customers/{} - Getting customer by identification", identification);
        return customerUseCase.getCustomerByIdentification(identification)
                .map(response -> ResponseEntity.ok(JsonDtoResponse.ok("Customer found", response)));
    }

    @DeleteMapping("/{identification}")
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> deleteCustomerByIdentification(
            @PathVariable(name = "identification") String identification) {
        return customerUseCase.deleteCustomerByIdentification(identification)
                .thenReturn(ResponseEntity.ok(JsonDtoResponse.ok("Customer deleted successfully")));
    }

}
