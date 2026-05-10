package nttdata.test.microservice_customer.application.ports.out;

import nttdata.test.microservice_customer.domain.models.CreateCustomerCommand;
import nttdata.test.microservice_customer.domain.models.UpdateCustomerCommand;
import nttdata.test.microservice_customer.domain.models.CustomerResponse;
import reactor.core.publisher.Mono;

public interface CustomerUseCase {

    Mono<CustomerResponse> createCustomer(CreateCustomerCommand command);

    Mono<CustomerResponse> getCustomerByIdentification(String identification);

    Mono<CustomerResponse> updateCustomerByIdentification(String identification, UpdateCustomerCommand command);

    Mono<Boolean> deleteCustomerByIdentification(String identification);
}
