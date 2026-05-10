package nttdata.test.microservice_customer.infrastructure.adapters.in.valid;

public final class CustomerValidation {
    private CustomerValidation() {
    }

    public static final String GENDER_REGEX = "^(M|F|OTHER)$";
    public static final String PHONE_REGEX = "^\\d{10}$";
}
