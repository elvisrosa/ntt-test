package nttdata.test.microservice_customer.infrastructure.adapters.in.web;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nttdata.test.microservice_customer.application.ports.out.MovementUseCase;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.dto.response.JsonDtoResponse;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.export.ReportExporterFactory;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final MovementUseCase movementUseCase;
    private final ReportExporterFactory exporterFactory;

    @GetMapping("/{clientIdentification}")
    public Mono<ResponseEntity<JsonDtoResponse<List<Movement>>>> getMovementsByClientAndDate(
            @PathVariable("clientIdentification") String clientIdentification,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        log.info("[REPORT-CONTROLLER] Fetching movements for client: {}, startDate: {}, endDate: {}",
                clientIdentification, startDate, endDate);

        return movementUseCase.getMovementsByClientIdentificationAndDateRange(clientIdentification,
                startDate, endDate)
                .collectList()
                .map(movements -> {
                    log.info("[REPORT-CONTROLLER] Retrieved {} movements for client: {}",
                            movements.size(), clientIdentification);
                    return ResponseEntity.ok(JsonDtoResponse.ok("Movements retrieved", movements));
                })
                .onErrorResume(error -> {
                    log.error("[REPORT-CONTROLLER] Error fetching movements: {}", error.getMessage(), error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(JsonDtoResponse.error("Error fetching movements: " + error.getMessage(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value())));
                });
    }

    @GetMapping("/{clientIdentification}/download")
    public Mono<ResponseEntity<?>> downloadMovementsReport(
            @PathVariable("clientIdentification") String clientIdentification,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "format", defaultValue = "json") String format) {

        log.info("[REPORT-CONTROLLER] Downloading movements for client: {}, format: {}",
                clientIdentification, format);

        return movementUseCase.getMovementsByClientIdentificationAndDateRange(clientIdentification,
                startDate, endDate)
                .collectList()
                .flatMap(movements -> {
                    log.info("[REPORT-CONTROLLER] Exporting {} movements in {} format for client: {}",
                            movements.size(), format, clientIdentification);
                    var exporter = exporterFactory.getExporter(format);
                    var reportResult = exporter.export(movements, clientIdentification);
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(reportResult.getContentType()));
                    headers.setContentDispositionFormData("attachment", reportResult.getFilename());
                    
                    ResponseEntity<?> response = new ResponseEntity<>(reportResult.getData(), headers, HttpStatus.OK);
                    return Mono.<ResponseEntity<?>>just(response);
                })
                .onErrorResume(error -> {
                    log.error("[REPORT-CONTROLLER] Error downloading report: {}", error.getMessage(), error);
                    
                    ResponseEntity<?> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(JsonDtoResponse.error("Error downloading report: " + error.getMessage(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value()));
                    return Mono.just(errorResponse);
                });
    }
}
