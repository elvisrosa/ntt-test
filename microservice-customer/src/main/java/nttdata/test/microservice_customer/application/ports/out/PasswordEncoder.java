package nttdata.test.microservice_customer.application.ports.out;

public interface PasswordEncoder {

    String encode(String rawPassword);
}
