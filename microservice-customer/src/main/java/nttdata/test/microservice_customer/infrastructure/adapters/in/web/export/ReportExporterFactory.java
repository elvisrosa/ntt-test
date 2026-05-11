package nttdata.test.microservice_customer.infrastructure.adapters.in.web.export;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportExporterFactory {
    
    private final JsonReportExporter jsonExporter;
    private final ExcelReportExporter excelExporter;
    
    public ReportExporter getExporter(String format) {
        String normalizedFormat = (format != null ? format.toLowerCase() : "json");
        log.debug("[EXPORTER-FACTORY] Getting exporter for format: {}", normalizedFormat);
        Map<String, ReportExporter> exporters = Map.of(
            "json", jsonExporter,
            "excel", excelExporter,
            "xlsx", excelExporter
        );
        
        return exporters.getOrDefault(normalizedFormat, jsonExporter);
    }
}

