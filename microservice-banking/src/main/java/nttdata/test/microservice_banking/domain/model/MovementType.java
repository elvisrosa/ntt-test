package nttdata.test.microservice_banking.domain.model;

import nttdata.test.microservice_banking.domain.exception.ExceptionCustom;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum MovementType {
    DEBIT("Debito"),
    CREDIT("Credito");

    private final String description;

    MovementType(String description) {
        this.description = description;
    }

    public static MovementType from(String value) {
        if (value == null) {
            throw new ExceptionCustom("Tipo de movimiento no puede ser nulo", HttpStatus.BAD_REQUEST.value());
        }
        String v = value.trim().toUpperCase();
        return switch (v) {
            case "DEBIT" -> DEBIT;
            case "CREDIT" -> CREDIT;
            default -> throw new ExceptionCustom("[DEBIT-CREDIT] Tipo de movimiento inválido: " + value,
                    HttpStatus.BAD_REQUEST.value());
        };
    }
}
