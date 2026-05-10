package nttdata.test.microservice_customer.infrastructure.adapters.out.mapper;

import org.springframework.stereotype.Component;

import nttdata.test.microservice_customer.infrastructure.persistence.r2dbc.ClientEntity;
import nttdata.test.microservice_customer.domain.models.Client;

@Component
public class ClientEntityMapper {

    public ClientEntity toEntity(Client client) {
        return ClientEntity.builder()
                .id(client.getId())
                .name(client.getName())
                .gender(client.getGender())
                .identification(client.getIdentification())
                .address(client.getAddress())
                .phone(client.getPhone())
                .password(client.getPassword())
                .status(client.getStatus())
                .build();
    }

    public Client toDomain(ClientEntity entity) {
        Client client = new Client(
                entity.getName(),
                entity.getGender(),
                entity.getIdentification(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getPassword(),
                entity.getStatus());
        client.setId(entity.getId());
        return client;
    }

}
