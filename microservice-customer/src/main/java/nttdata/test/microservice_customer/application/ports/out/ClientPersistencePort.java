package nttdata.test.microservice_customer.application.ports.out;

import nttdata.test.microservice_customer.domain.models.Client;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientPersistencePort {

    Mono<Client> saveClient(Client client);

    Mono<Client> findClientById(UUID id);

    Mono<Client> findClientByIdentification(String identification);

    Mono<Client> findClientByIdentificationAndPassword(String identification, String password);

    Flux<Client> findAllClients();

    Mono<Client> updateClient(Client client);

    Mono<Void> deleteClientById(UUID id);
}
