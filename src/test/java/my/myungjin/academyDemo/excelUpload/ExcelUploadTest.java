package my.myungjin.academyDemo.excelUpload;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExcelUploadTest {


    @Test
    void 엑셀_파일을_읽을_수_있다() throws IOException {
        URL testExcelFile = getClass().getResource("/test.xlsx");
        MatcherAssert.assertThat(testExcelFile, is(notNullValue()));

        File file = new File(testExcelFile.getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

        int rowIdx = 2;

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        for( ; rowIdx < rows; rowIdx++){
            XSSFRow row = sheet.getRow(rowIdx);
            XSSFCell cell1 = row.getCell(0);
            XSSFCell cell2 = row.getCell(1);

            log.info("Cell 1 Value: {}, Cell 2 Value: {}", cell1.getRawValue(), cell2.getRawValue());
        }
    }

}
