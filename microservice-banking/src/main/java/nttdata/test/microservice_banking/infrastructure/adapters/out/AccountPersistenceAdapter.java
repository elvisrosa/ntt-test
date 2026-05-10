package nttdata.test.microservice_banking.infrastructure.adapters.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_banking.application.ports.out.AccountPersistencePort;
import nttdata.test.microservice_banking.domain.exception.ExceptionCustom;
import nttdata.test.microservice_banking.domain.model.Account;
import nttdata.test.microservice_banking.infrastructure.adapters.out.mapper.AccountEntityMapper;
import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.ClientEntity;
import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.AccountEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountPersistenceAdapter implements AccountPersistencePort {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountEntityMapper accountEntityMapper;

    @Transactional(rollbackFor = { ExceptionCustom.class, Exception.class })
    @Override
    public Mono<Account> saveAccount(Account account) {
        AccountEntity entity = accountEntityMapper.toEntity(account);
        return accountRepository.save(entity)
                .map(accountEntityMapper::toDomain)
                .doOnNext(a -> log.info("[PERSISTENCE-SAVE] Saved account id={} accountNumber={}",
                        a.getId(),
                        a.getAccountNumber()));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountEntityMapper::toDomain)
                .doOnNext(a -> log.info("[PERSISTENCE-FIND] Found account accountNumber={} id={}",
                        accountNumber,
                        a.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Account> findByClientIdentification(String identification) {
        return clientRepository.findByIdentification(identification)
                .flatMapMany(clientEntity -> accountRepository
                        .findByClientId(clientEntity.getId()))
                .map(accountEntityMapper::toDomain)
                .doOnNext(a -> log.debug(
                        "[PERSISTENCE-FIND] Found account for identification={} accountNumber={}",
                        identification, a.getAccountNumber()));
    }

    @Transactional(rollbackFor = { ExceptionCustom.class, Exception.class })
    @Override
    public Mono<Account> updateAccount(Account account) {
        return accountRepository.save(accountEntityMapper.toEntity(account))
                .map(accountEntityMapper::toDomain)
                .doOnNext(a -> log.info("[PERSISTENCE-UPDATE] Updated account id={} accountNumber={}",
                        a.getId(),
                        a.getAccountNumber()));
    }

    @Transactional(rollbackFor = { ExceptionCustom.class, Exception.class })
    @Override
    public Mono<Void> deleteAccountById(UUID id) {
        return accountRepository.deleteById(id)
                .doOnSuccess(v -> log.info("[PERSISTENCE-DELETE] Deleted account id={}", id));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<UUID> findClientIdByIdentification(String identification) {
        return clientRepository.findByIdentification(identification)
                .map(ClientEntity::getId)
                .doOnNext(id -> log.debug(
                        "[PERSISTENCE-FIND] Resolved client id={} for identification={}", id,
                        identification));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Account> findById(UUID id) {
        return accountRepository.findById(id)
                .map(accountEntityMapper::toDomain)
                .doOnNext(a -> log.debug("[PERSISTENCE-FIND] Found account by id={} accountNumber={}",
                        id,
                        a.getAccountNumber()));
    }

}
