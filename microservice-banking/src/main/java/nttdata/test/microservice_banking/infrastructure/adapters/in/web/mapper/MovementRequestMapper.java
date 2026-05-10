package nttdata.test.microservice_banking.infrastructure.adapters.in.web.mapper;

import org.springframework.stereotype.Component;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request.CreateMovement;
import nttdata.test.microservice_banking.domain.model.CreateMovementCommand;

@Component
public class MovementRequestMapper {

    public CreateMovementCommand toCreateCommand(String accountNumber, CreateMovement r) {
        return new CreateMovementCommand(accountNumber, r.amount(), r.type(), r.description());
    }

}
