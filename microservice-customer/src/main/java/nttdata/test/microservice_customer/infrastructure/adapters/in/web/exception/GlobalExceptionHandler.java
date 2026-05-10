package nttdata.test.microservice_customer.infrastructure.adapters.in.web.exception;

import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.domain.exception.ClientNotFoundException;
import nttdata.test.microservice_customer.domain.exception.DuplicateIdentificationException;
import nttdata.test.microservice_customer.domain.exception.ExceptionCustom;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import lombok.Builder;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> handleCustomerNotFound(NotFoundException ex) {
        log.warn("Customer not found: {}", ex.getMessage());

        JsonDtoResponse<Void> error = JsonDtoResponse.<Void>notFound("Resource not found", "NOT_FOUND");
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> handleNoResourceFound(NoResourceFoundException ex,
            ServerWebExchange exchange) {
        log.warn("No resource found for request: {}", exchange.getRequest().getURI());

        String url = exchange.getRequest().getURI().getPath();
        log.warn("No resource found for URL: {}", url);

        JsonDtoResponse<Void> error = JsonDtoResponse.<Void>notFound("Resource not found: " + url, null);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(DuplicateIdentificationException.class)
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> handleDuplicateIdentification(
            DuplicateIdentificationException ex) {
        log.warn("Duplicate customer identification: {}", ex.getMessage());

        JsonDtoResponse<Void> error = JsonDtoResponse.<Void>conflict(ex.getMessage(), "DUPLICATE_IDENTIFICATION");
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> handleIllegalArgument(IllegalArgumentException ex) {
        JsonDtoResponse<Void> error = JsonDtoResponse.<Void>badRequest(ex.getMessage(), "BAD_REQUEST");
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<JsonDtoResponse<List<ValidationErrorItem>>>> handleValidationErrors(
            WebExchangeBindException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        List<ValidationErrorItem> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ValidationErrorItem.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();

        JsonDtoResponse<List<ValidationErrorItem>> error = JsonDtoResponse.<List<ValidationErrorItem>>badRequest(
                "Validation failed", validationErrors,
                "VALIDATION_ERROR");

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> handleGenericError(Exception ex) {
        log.error("Unexpected error", ex);

        JsonDtoResponse<Void> error = JsonDtoResponse.<Void>internalServerError("An unexpected error occurred",
                "INTERNAL_SERVER_ERROR");

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public Mono<ResponseEntity<JsonDtoResponse<Void>>> handleClientNotFoundException(ClientNotFoundException ex) {
        log.warn("Client not found: {}", ex.getMessage());

        JsonDtoResponse<Void> error = JsonDtoResponse.<Void>notFound(ex.getMessage(), null);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(ExceptionCustom.class)
    public Mono<ResponseEntity<JsonDtoResponse<Object>>> handleExceptionCustom(ExceptionCustom ex) {
        log.error("Error Custom", ex);
        JsonDtoResponse<Object> error = JsonDtoResponse.<Object>error(
                ex.getMessage(),
                ex.getStatus(),
                ex.getResult(),
                ex.getErrorCode());

        return Mono.just(ResponseEntity.status(HttpStatus.valueOf(ex.getStatus())).body(error));
    }

    @Builder
    public record ValidationErrorItem(
            String field,
            String message) {
    }
}
