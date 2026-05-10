package nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import lombok.Data;

// @JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JsonDtoResponse<T> {

    private int statusCode;

    private String message;

    private T result;

    private String details;

    private JsonDtoResponse(String message, int statusCode, T result, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.result = result;
        this.details = details;
    }

    // Jackson requires a public no-arg constructor for deserialization
    public JsonDtoResponse() {
    }

    public Mono<ResponseEntity<JsonDtoResponse<T>>> toResponseEntity() {
        return Mono.just(ResponseEntity.status(this.statusCode).body(this));
    }

    public static <T> JsonDtoResponse<T> ok(String message) {
        return new JsonDtoResponse<>(message, HttpStatus.OK.value(), null, null);
    }

    public static <T> JsonDtoResponse<T> ok(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.OK.value(), result, (String) null);
    }

    public static <T> JsonDtoResponse<T> created(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.CREATED.value(), result, (String) null);
    }

    public static <T> JsonDtoResponse<T> accepted(String message, T result) {
        return new JsonDtoResponse<>(message, HttpStatus.ACCEPTED.value(), result, (String) null);
    }

    public static <T> JsonDtoResponse<T> noContent() {
        return new JsonDtoResponse<>((String) null, HttpStatus.NO_CONTENT.value(), null, (String) null);
    }

    public static <T> JsonDtoResponse<T> badRequest(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.BAD_REQUEST.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> badRequest(String message, T result, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.BAD_REQUEST.value(), result, errorCode);
    }

    public static <T> JsonDtoResponse<T> unauthorized(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.UNAUTHORIZED.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> forbidden(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.FORBIDDEN.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> notFound(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.NOT_FOUND.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> conflict(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.CONFLICT.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> internalServerError(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> notImplemented(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.NOT_IMPLEMENTED.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> serviceUnavailable(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.SERVICE_UNAVAILABLE.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> timeout(String message, String errorCode) {
        return new JsonDtoResponse<>(message, HttpStatus.GATEWAY_TIMEOUT.value(), null, errorCode);
    }

    public static <T> JsonDtoResponse<T> error(String message, int status) {
        return new JsonDtoResponse<>(message, status, null, null);
    }

    public static <T> JsonDtoResponse<T> error(String message, int status, T result, String errorCode) {
        return new JsonDtoResponse<>(message, status, result, errorCode);
    }

}
