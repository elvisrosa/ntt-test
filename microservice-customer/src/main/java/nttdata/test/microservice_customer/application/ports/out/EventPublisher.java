package nttdata.test.microservice_customer.application.ports.out;

import reactor.core.publisher.Mono;

public interface EventPublisher {
    Mono<Void> publishCustomerCreated(Object event);

    Mono<Void> publishCustomerUpdated(Object event);

    Mono<Void> publishCustomerDeleted(Object event);
}
