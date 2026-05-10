package nttdata.test.microservice_customer.infrastructure.adapters.in.web.mapper;

import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request.CreateCustomer;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request.UpdateCustomer;
import nttdata.test.microservice_customer.domain.models.CreateCustomerCommand;
import nttdata.test.microservice_customer.domain.models.UpdateCustomerCommand;
import org.springframework.stereotype.Component;

@Component
public class CustomerRequestMapper {

    public CreateCustomerCommand toCreateCommand(CreateCustomer request) {
        return new CreateCustomerCommand(
                request.name(),
                request.gender(),
                request.identification(),
                request.address(),
                request.phoneNumber(),
                request.password());
    }

    public UpdateCustomerCommand toUpdateCommand(UpdateCustomer request) {
        return new UpdateCustomerCommand(
                request.name(),
                request.gender(),
                request.address(),
                request.phoneNumber(),
                request.password());
    }

}
