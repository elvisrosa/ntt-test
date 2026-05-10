package nttdata.test.microservice_customer.domain.models;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String identification,
        String address,
        String phone) {
}
