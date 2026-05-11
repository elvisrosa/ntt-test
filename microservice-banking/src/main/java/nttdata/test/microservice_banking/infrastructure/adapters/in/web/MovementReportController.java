package nttdata.test.microservice_banking.infrastructure.adapters.in.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import nttdata.test.microservice_banking.domain.model.Movement;
import nttdata.test.microservice_banking.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movements/reports")
@RequiredArgsConstructor
public class MovementReportController {

    private final nttdata.test.microservice_banking.application.ports.out.MovementUseCase movementUseCase;

    @GetMapping("/{clientId}")
    public Mono<ResponseEntity<JsonDtoResponse<List<Movement>>>> getMovementsByClientAndDate(
            @PathVariable("clientId") String clientId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(JsonDtoResponse.error("Invalid date format. Use yyyy-MM-dd",
                            HttpStatus.BAD_REQUEST.value())));
        }
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        return movementUseCase.getMovementsByClientIdentificationAndDateRange(clientId, startDateTime, endDateTime)
                .collectList()
                .map(movements -> ResponseEntity.ok(JsonDtoResponse.ok("Movements retrieved", movements)));
    }

}
