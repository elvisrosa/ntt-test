package nttdata.test.microservice_banking.domain.exception;

import org.springframework.http.HttpStatus;

public class ExceptionInsufficientFunds extends ExceptionCustom {

    public ExceptionInsufficientFunds() {
        super("Saldo insuficiente", HttpStatus.BAD_REQUEST.value(), "INSUFFICIENT_FUNDS");
    }

}
