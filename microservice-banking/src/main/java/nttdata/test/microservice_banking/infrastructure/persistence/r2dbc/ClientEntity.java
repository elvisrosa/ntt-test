package nttdata.test.microservice_banking.infrastructure.persistence.r2dbc;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("client")
public class ClientEntity extends Person {

    @Id
    private UUID id;

    private String password;

    private Boolean status;
}
