package nttdata.test.microservice_customer.infrastructure.adapters.in.web.mapper;

import org.springframework.stereotype.Component;

import nttdata.test.microservice_customer.domain.models.CreateMovement;
import nttdata.test.microservice_customer.domain.models.CreateMovementCommand;

@Component
public class MovementRequestMapper {

    public CreateMovementCommand toCreateCommand(String accountNumber, CreateMovement r) {
        return new CreateMovementCommand(accountNumber, r.amount(), r.type(), r.description());
    }

}
