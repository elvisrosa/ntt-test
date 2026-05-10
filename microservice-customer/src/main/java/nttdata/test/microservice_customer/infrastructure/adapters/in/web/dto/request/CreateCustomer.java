package nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import nttdata.test.microservice_customer.infrastructure.adapters.in.valid.CustomerValidation;

@Builder
public record CreateCustomer(
        @NotBlank(message = "Name is required") @Size(max = 100, message = "Name must not exceed 100 characters") String name,

        @NotBlank(message = "Gender is required") @Pattern(regexp = CustomerValidation.GENDER_REGEX, message = "Gender must be M, F or OTHER") String gender,

        @NotBlank(message = "Identification is required") @Size(max = 30, message = "Identification must not exceed 30 characters") String identification,

        @NotBlank(message = "Address is required") @Size(max = 200, message = "Address must not exceed 200 characters") String address,

        @NotBlank(message = "Phone number is required") @Pattern(regexp = CustomerValidation.PHONE_REGEX, message = "Phone number must contain exactly 10 digits") String phoneNumber,
        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password) {
}
