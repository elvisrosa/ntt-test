package nttdata.test.microservice_customer.domain.exception;

public class DuplicateIdentificationException extends RuntimeException {
    public DuplicateIdentificationException(String identification) {
        super(String.format("Customer already exists with identification: %s", identification));
    }
}
