package nttdata.test.microservice_customer.application.mapper;

import org.springframework.stereotype.Component;

import nttdata.test.microservice_customer.domain.models.Client;
import nttdata.test.microservice_customer.domain.models.CustomerResponse;

@Component
public class CustomerDomainMapper {

    public CustomerResponse toCustomerResponse(Client client) {
        return new CustomerResponse(
                client.getId(),
                client.getName(),
                client.getIdentification(),
                client.getAddress(),
                client.getPhone());
    }

}
