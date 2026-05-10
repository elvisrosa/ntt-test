package nttdata.test.microservice_customer.infrastructure.adapters.out;

import java.util.UUID;

import nttdata.test.microservice_customer.domain.models.Client;
import nttdata.test.microservice_customer.application.ports.out.ClientPersistencePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class ClientPersistenceAdapter implements ClientPersistencePort {

    private final ClientRepository clientRepository;
    private final nttdata.test.microservice_customer.infrastructure.adapters.out.mapper.ClientEntityMapper clientEntityMapper;

    @Override
    public Mono<Client> saveClient(Client client) {
        return clientRepository.save(clientEntityMapper.toEntity(client))
                .map(clientEntityMapper::toDomain)
                .doOnNext(c -> log.info("[PERSISTENCE-SAVE] Saved client id={} identification={}", c.getId(),
                        c.getIdentification()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Client> findClientById(UUID id) {
        return clientRepository.findById(id)
                .map(clientEntityMapper::toDomain)
                .doOnNext(c -> log.info("[PERSISTENCE-FIND] Found client by id={} identification={}", c.getId(),
                        c.getIdentification()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Client> findClientByIdentification(String identification) {
        return clientRepository.findByIdentification(identification)
                .map(clientEntityMapper::toDomain)
                .doOnNext(c -> log.info("[PERSISTENCE-FIND] Found client by identification={} id={}", identification,
                        c.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Client> findClientByIdentificationAndPassword(String identification, String password) {
        return clientRepository.findByIdentificationAndPassword(identification, password)
                .map(clientEntityMapper::toDomain)
                .doOnNext(c -> log.info("[PERSISTENCE-FIND] Authenticated client id={} identification={}", c.getId(),
                        c.getIdentification()));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Client> findAllClients() {
        return clientRepository.findAll()
                .map(clientEntityMapper::toDomain)
                .doOnNext(c -> log.debug("[PERSISTENCE-FIND-ALL] client id={} identification={}", c.getId(),
                        c.getIdentification()));
    }

    @Override
    public Mono<Client> updateClient(Client client) {
        return clientRepository.save(clientEntityMapper.toEntity(client))
                .map(clientEntityMapper::toDomain)
                .doOnNext(c -> log.info("[PERSISTENCE-UPDATE] Updated client id={} identification={}", c.getId(),
                        c.getIdentification()));
    }

    @Override
    public Mono<Void> deleteClientById(UUID id) {
        return clientRepository.deleteById(id)
                .doOnSuccess(v -> log.info("[PERSISTENCE-DELETE] Deleted client id={}", id));
    }

}
