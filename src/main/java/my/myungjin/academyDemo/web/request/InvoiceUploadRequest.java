package my.myungjin.academyDemo.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString
@Getter
@NoArgsConstructor
public class InvoiceUploadRequest {

    private List<InvoiceRequest> invoices = new ArrayList<>();

    public InvoiceUploadRequest readInvoices (MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        int rowIdx = 2;
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        for( ; rowIdx < rows; rowIdx++){
            XSSFRow row = sheet.getRow(rowIdx);
            XSSFCell orderIdCell = row.getCell(0);
            XSSFCell invoiceCell = row.getCell(1);

            log.info("deliveryId: {}, invoice: {}", orderIdCell.getStringCellValue(), invoiceCell.getRawValue());

            this.invoices.add(new InvoiceRequest(orderIdCell.getStringCellValue(), invoiceCell.getRawValue()));
        }
        return this;
    }

}
