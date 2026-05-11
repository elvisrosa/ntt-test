package nttdata.test.microservice_customer.infrastructure.adapters.in.web.export;

import java.util.List;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.domain.exception.ExceptionCustom;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.export.dto.ReportResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
@Slf4j
public class JsonReportExporter implements ReportExporter {

    private final ObjectMapper objectMapper;

    public JsonReportExporter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public ReportResult export(List<Movement> movements, String clientIdentification) {
        log.debug("[JSON-EXPORTER] Exporting {} movements as JSON for client: {}", movements.size(),
                clientIdentification);

        try {
            byte[] jsonData = objectMapper.writeValueAsBytes(movements);

            return ReportResult.builder()
                    .data(jsonData)
                    .filename("movements_" + clientIdentification + ".json")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();

        } catch (Exception e) {
            log.error("[JSON-EXPORTER] Error exporting to JSON: {}", e.getMessage(), e);
            throw new ExceptionCustom("Error exporting to JSON: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
