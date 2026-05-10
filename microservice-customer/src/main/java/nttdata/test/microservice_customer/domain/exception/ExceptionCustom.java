package nttdata.test.microservice_customer.domain.exception;

import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCustom extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;
    private final String errorCode;
    private final Object result;

    public ExceptionCustom(String message, HttpStatus status) {
        super(message);
        this.status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status.value();
        this.errorCode = null;
        this.result = null;
    }

    public ExceptionCustom(String message, int status) {
        super(message);
        this.status = status;
        this.errorCode = null;
        this.result = null;
    }

    public ExceptionCustom(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : status.value();
        this.errorCode = errorCode;
        this.result = null;
    }

    public ExceptionCustom(String message, int status, String errorCode, Object result) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object getResult() {
        return result;
    }
}
