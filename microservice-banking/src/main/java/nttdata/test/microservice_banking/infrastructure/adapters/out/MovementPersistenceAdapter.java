package nttdata.test.microservice_banking.infrastructure.adapters.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_banking.application.ports.out.MovementPersistencePort;
import nttdata.test.microservice_banking.domain.exception.ExceptionCustom;
import nttdata.test.microservice_banking.domain.model.Movement;
import nttdata.test.microservice_banking.infrastructure.adapters.out.mapper.MovementEntityMapper;
import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.MovementEntity;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class MovementPersistenceAdapter implements MovementPersistencePort {

    private final MovementRepository movementRepository;
    private final MovementEntityMapper mapper;

    @Override
    @Transactional(rollbackFor = { ExceptionCustom.class, Exception.class })
    public Mono<Movement> saveMovement(Movement movement) {
        MovementEntity entity = mapper.toEntity(movement);
        return movementRepository.save(entity)
                .map(mapper::toDomain)
                .doOnNext(m -> log.info("[PERSISTENCE-SAVE] Movement saved id={} accountId={} amount={}", m.getId(),
                        m.getAccountId(), m.getAmount()));
    }

    @Override
    public Flux<Movement> findByAccountId(UUID accountId) {
        return movementRepository.findByAccountId(accountId).map(mapper::toDomain);
    }

}
