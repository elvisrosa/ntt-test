package nttdata.test.microservice_banking.infrastructure.adapters.in.web.mapper;

import org.springframework.stereotype.Component;

import nttdata.test.microservice_banking.domain.model.CreateAccountCommand;
import nttdata.test.microservice_banking.domain.model.UpdateAccountCommand;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request.CreateAccount;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.request.UpdateAccount;

@Component
public class AccountRequestMapper {
    public CreateAccountCommand toCreateCommand(CreateAccount request) {
        return new CreateAccountCommand(
                request.accountNumber(),
                request.accountType(),
                request.initialBalance(),
                request.initialBalance(),
                request.customerIdentification());
    }

    public UpdateAccountCommand toUpdateCommand(UpdateAccount request) {
        return new UpdateAccountCommand(
                request.accountType(),
                request.currentBalance());
    }
}
