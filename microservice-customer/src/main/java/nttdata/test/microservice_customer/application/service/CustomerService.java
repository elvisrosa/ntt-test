package nttdata.test.microservice_customer.application.service;

import nttdata.test.microservice_customer.domain.models.CreateCustomerCommand;
import nttdata.test.microservice_customer.domain.models.CustomerResponse;
import nttdata.test.microservice_customer.domain.models.UpdateCustomerCommand;
import nttdata.test.microservice_customer.domain.exception.ClientNotFoundException;
import nttdata.test.microservice_customer.domain.exception.DuplicateIdentificationException;
import nttdata.test.microservice_customer.domain.models.Client;
import nttdata.test.microservice_customer.application.ports.out.ClientPersistencePort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_customer.application.ports.out.CustomerUseCase;
import nttdata.test.microservice_customer.application.ports.out.PasswordEncoder;
import nttdata.test.microservice_customer.application.mapper.CustomerDomainMapper;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService implements CustomerUseCase {

    private final ClientPersistencePort clientPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDomainMapper customerDomainMapper;

    @Override
    public Mono<CustomerResponse> createCustomer(CreateCustomerCommand customer) {
        log.info("[CREATE-CUSTOMER] Start - create request for identification={}", customer.identification());
        return clientPersistencePort.findClientByIdentification(customer.identification())
                .flatMap(existing -> Mono.<CustomerResponse>error(
                        new DuplicateIdentificationException(existing.getIdentification())))
                .switchIfEmpty(Mono.fromSupplier(() -> new Client(
                        customer.name(),
                        customer.gender(),
                        customer.identification(),
                        customer.address(),
                        customer.phone(),
                        passwordEncoder.encode(customer.password()),
                        Boolean.TRUE))
                        .flatMap(clientPersistencePort::saveClient)
                        .map(customerDomainMapper::toCustomerResponse))
                .doOnNext(resp -> log.info("[CREATE-CUSTOMER] Success - created id={} identification={}", resp.id(),
                        resp.identification()))
                .doOnError(err -> log.warn("[CREATE-CUSTOMER] Failed - identification={} - {}",
                        customer.identification(), err.getMessage()));
    }

    @Override
    public Mono<CustomerResponse> getCustomerByIdentification(String identification) {
        return clientPersistencePort.findClientByIdentification(identification)
                .switchIfEmpty(Mono.error(
                        new ClientNotFoundException(identification)))
                .map(customerDomainMapper::toCustomerResponse);
    }

    @Override
    public Mono<CustomerResponse> updateCustomerByIdentification(String identification, UpdateCustomerCommand command) {
        log.info("[UPDATE-CUSTOMER] Start - update request for identification={}", identification);
        return clientPersistencePort.findClientByIdentification(identification)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[UPDATE-CUSTOMER] NotFound - identification={}", identification);
                    return Mono
                            .error(new ClientNotFoundException(
                                    identification));
                }))
                .flatMap(existing -> {
                    existing.setName(command.name());
                    existing.setGender(command.gender());
                    existing.setAddress(command.address());
                    existing.setPhone(command.phone());
                    if (command.password() != null && !command.password().isEmpty()) {
                        existing.setPassword(passwordEncoder.encode(command.password()));
                    }
                    return clientPersistencePort.updateClient(existing).map(customerDomainMapper::toCustomerResponse);
                })
                .doOnNext(resp -> log.info("[UPDATE-CUSTOMER] Success - updated id={} identification={}", resp.id(),
                        resp.identification()))
                .doOnError(err -> log.warn("[UPDATE-CUSTOMER] Failed - identification={} - {}", identification,
                        err.getMessage()));

    }

    @Override
    public Mono<Boolean> deleteCustomerByIdentification(String identification) {
        log.info("[DELETE-CUSTOMER] Start - delete request for identification={}", identification);
        return clientPersistencePort.findClientByIdentification(identification)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("[DELETE-CUSTOMER] NotFound - identification={}", identification);
                    return Mono
                            .error(new ClientNotFoundException(
                                   identification));
                }))
                .flatMap(existing -> {
                    String nextIdentification = existing.getIdentification() + "_d" + UUID.randomUUID();
                    String trim30 = nextIdentification.length() > 30 ? nextIdentification.substring(0, 30)
                            : nextIdentification;
                    existing.setStatus(Boolean.FALSE);
                    existing.setPassword("logically_deleted");
                    existing.setIdentification(trim30);
                    return clientPersistencePort.updateClient(existing)
                            .thenReturn(Boolean.TRUE);
                })
                .switchIfEmpty(Mono.just(Boolean.FALSE))
                .doOnNext(deleted -> {
                    if (Boolean.TRUE.equals(deleted))
                        log.info("[DELETE-CUSTOMER] Success - deleted identification={}", identification);
                    else
                        log.warn("[DELETE-CUSTOMER] NotFound - identification={}", identification);
                });
    }

}
