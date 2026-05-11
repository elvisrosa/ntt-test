package nttdata.test.microservice_customer.infrastructure.adapters.in.web.export.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResult {
    private byte[] data;
    private String filename;
    private String contentType;
}
