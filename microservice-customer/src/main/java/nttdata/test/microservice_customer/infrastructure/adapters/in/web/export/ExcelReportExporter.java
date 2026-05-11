package nttdata.test.microservice_customer.infrastructure.adapters.in.web.export;

import java.io.ByteArrayOutputStream;
import java.util.List;
import nttdata.test.microservice_customer.domain.models.Movement;
import nttdata.test.microservice_customer.domain.exception.ExceptionCustom;
import nttdata.test.microservice_customer.infrastructure.adapters.in.web.export.dto.ReportResult;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExcelReportExporter implements ReportExporter {

    @Override
    public ReportResult export(List<Movement> movements, String clientIdentification) {
        log.debug("[EXCEL-EXPORTER] Exporting {} movements as Excel for client: {}", movements.size(),
                clientIdentification);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Movements Report");
            createHeader(sheet);

            int rowNum = 1;
            for (Movement movement : movements) {
                createRow(sheet, rowNum++, movement);
            }

            autoSizeColumns(sheet, 6);

            workbook.write(outputStream);
            byte[] excelData = outputStream.toByteArray();

            return ReportResult.builder()
                    .data(excelData)
                    .filename("movements_" + clientIdentification + ".xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .build();

        } catch (Exception e) {
            log.error("[EXCEL-EXPORTER] Error exporting to Excel: {}", e.getMessage(), e);
            throw new ExceptionCustom("Error exporting to Excel: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getFormat() {
        return "excel";
    }

    private void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Account ID", "Movement Type", "Amount", "Date", "Balance After", "Description"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void createRow(Sheet sheet, int rowNum, Movement movement) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0)
                .setCellValue(movement.getAccountId() != null ? movement.getAccountId().toString() : "");
        row.createCell(1).setCellValue(movement.getType() != null ? movement.getType() : "");
        row.createCell(2).setCellValue(movement.getAmount() != null ? movement.getAmount().doubleValue() : 0.0);
        row.createCell(3)
                .setCellValue(movement.getCreatedAt() != null ? movement.getCreatedAt().toString() : "");
        row.createCell(4).setCellValue(
                movement.getBalanceAfter() != null ? movement.getBalanceAfter().doubleValue() : 0.0);
        row.createCell(5).setCellValue(movement.getDescription() != null ? movement.getDescription() : "");
    }

    private void autoSizeColumns(Sheet sheet, int count) {
        for (int i = 0; i < count; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
