package nttdata.test.microservice_banking.domain.exception;

import org.springframework.http.HttpStatus;

public class ExceptionCustom extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;
    private final String errorCode;

    public ExceptionCustom(String message, HttpStatus status) {
        super(message);
        this.status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status.value();
        this.errorCode = null;
    }

    public ExceptionCustom(String message, int status) {
        super(message);
        this.status = status;
        this.errorCode = null;
    }

    public ExceptionCustom(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status.value();
        this.errorCode = errorCode;
    }

    public ExceptionCustom(String message, int status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
