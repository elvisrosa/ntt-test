package nttdata.test.microservice_customer.domain.models;

import lombok.Builder;

@Builder
public record UpdateCustomerCommand(
        String name,
        String gender,
        String address,
        String phone,
        String password) {

    public UpdateCustomerCommand {
        validateName(name);
        validateGender(gender);
        validateAddress(address);
        validatePhone(phone);
        validatePassword(password);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name must not exceed 100 characters");
        }
    }

    private static void validateGender(String gender) {
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender is required");
        }
        if (!gender.matches("^(M|F|OTHER)$")) {
            throw new IllegalArgumentException("Gender must be M, F or OTHER");
        }
    }

    private static void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (address.length() > 200) {
            throw new IllegalArgumentException("Address must not exceed 200 characters");
        }
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (!phone.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Phone number must contain exactly 10 digits");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
