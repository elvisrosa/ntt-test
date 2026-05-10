package nttdata.test.microservice_customer.domain.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String identification) {
        super(String.format("Customer not found with identification: %s", identification));
    }
}
