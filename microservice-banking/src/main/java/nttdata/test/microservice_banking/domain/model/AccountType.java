package nttdata.test.microservice_banking.domain.model;

import nttdata.test.microservice_banking.domain.exception.ExceptionCustom;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum AccountType {
    AHORRO("Ahorro"),
    CORRIENTE("Corriente");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public static AccountType from(String value) {
        if (value == null) {
            throw new ExceptionCustom("Account type cannot be null", HttpStatus.BAD_REQUEST.value());
        }
        String v = value.trim().toUpperCase();
        return switch (v) {
            case "AHORRO" -> AHORRO;
            case "CORRIENTE" -> CORRIENTE;
            default -> throw new ExceptionCustom("[AHORRO-CORRIENTE] Invalid account type: " + value,
                    HttpStatus.BAD_REQUEST.value());
        };
    }
}
