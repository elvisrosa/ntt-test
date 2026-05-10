package nttdata.test.microservice_banking.infrastructure.adapters.out.mapper;

import nttdata.test.microservice_banking.infrastructure.persistence.r2dbc.MovementEntity;
import nttdata.test.microservice_banking.domain.model.Movement;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class MovementEntityMapper {

    public Movement toDomain(MovementEntity e) {
        if (e == null)
            return null;
        return new Movement(e.getId(), e.getAccountId(), e.getAmount(), e.getMovementType(), e.getBalanceAfter(),
                e.getDescription(), e.getCreatedAt());
    }

    public MovementEntity toEntity(Movement m) {
        if (m == null)
            return null;
        MovementEntity.MovementEntityBuilder b = MovementEntity.builder()
                .id(m.getId())
                .accountId(m.getAccountId())
                .movementType(m.getType())
                .amount(m.getAmount())
                .description(m.getDescription())
                .createdAt(m.getCreatedAt() == null ? LocalDateTime.now() : m.getCreatedAt());
        if (m.getBalanceAfter() != null)
            b.balanceAfter(m.getBalanceAfter());
        return b.build();
    }

}
