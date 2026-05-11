package nttdata.test.microservice_customer.infrastructure.adapters.in.web.export;

import java.util.List;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.export.dto.ReportResult;

public interface ReportExporter {
    
    ReportResult export(List<Movement> movements, String clientIdentification);
    
    String getFormat();
}

