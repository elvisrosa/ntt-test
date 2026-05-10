package nttdata.test.microservice_customer.infrastructure.adapters.out.mapper;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_customer.domain.exception.ExceptionCustom;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import reactor.core.publisher.Mono;

@Slf4j
@UtilityClass
public class WebClientErrorUtils {
    public static <T> Mono<T> handleErrorResponse(JsonDtoResponse<?> errorBody, int statusCode) {
        if (errorBody == null) {
            log.error("Error response vacío desde el servicio externo");
            return Mono.error(new ExceptionCustom(
                    "Servicio no disponible, intente nuevamente o contacte al administrador del sistema",
                    HttpStatus.SERVICE_UNAVAILABLE.value()));
        }

        String message = errorBody.getMessage();
        String detailError = errorBody.getDetails();
        Object errorData = errorBody.getResult();
        log.info("Error response body: message={}, details={}, errorData={}", message, detailError, errorData);
        if (message != null && !message.isBlank()) {
            return Mono.error(new ExceptionCustom(
                    message,
                    statusCode,
                    detailError,
                    errorData));
        }
        log.error("Error interno: message={}, details={}", message, detailError);
        return Mono.error(new ExceptionCustom(
                "Servicio no disponible, intente nuevamente o contacte al administrador del sistema",
                HttpStatus.SERVICE_UNAVAILABLE.value()));
    }

    public static <T> Mono<JsonDtoResponse<T>> handleResponse(ClientResponse response,
            ParameterizedTypeReference<JsonDtoResponse<T>> responseType) {
        log.info("Response status code: {}", response.statusCode());
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(responseType);
        } else {
            return response.bodyToMono(JsonDtoResponse.class)
                    .flatMap(errorBody -> handleErrorResponse(errorBody,
                            response.statusCode().value()));
        }
    }

}
