package my.myungjin.academyDemo.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.common.CommonCode;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ToString
@Getter
@NoArgsConstructor
public class OrderUploadRequest {

    private List<OrderRequest> orders = new ArrayList<>();

    public OrderUploadRequest readOrders(MultipartFile orderFile, List<CommonCode> columns) throws IOException {
        InputStream inputStream = orderFile.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        int rowIdx = 2;
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();
        Map<String, OrderRequest> orderMap = new HashMap<>();
        for( ; rowIdx < rows; rowIdx++){
            XSSFRow row = sheet.getRow(rowIdx);
            Map<String, String> map = new HashMap<>();
            for(CommonCode col : columns){
                int cellIdx = Integer.parseInt(col.getCode());
                String field = col.getNameEng();
                map.put(col.getNameEng(), row.getCell(cellIdx).toString());
            }
            String orderGubun = map.get("receiverName") + map.get("receiverAddr1") + map.get("receiverAddr2");
            CartRequest cartRequest = new CartRequest(map.get("itemId"), (int) Double.parseDouble(map.get("count")));
            orderMap.computeIfAbsent(
                    orderGubun,
                    s -> {
                        OrderRequest orderRequest = OrderRequest.builder()
                                .name(map.get("name"))
                                .tel(map.get("tel"))
                                .addr1(map.get("addr2"))
                                .receiverName(map.get("receiverName"))
                                .receiverTel(map.get("receiverTel"))
                                .receiverAddr1(map.get("receiverAddr1"))
                                .receiverAddr2(map.get("receiverAddr2"))
                                .items(new ArrayList<>())
                                .build();
                        orderRequest.addItem(cartRequest);
                        return orderRequest;
                    }
            );
            orderMap.computeIfPresent(
                    orderGubun,
                    (s, request) -> {
                        request.addItem(cartRequest);
                        return request;
                    }
            );

        }

        for(OrderRequest order : orderMap.values()){
            log.info("Order Request: {}", order);
        }
        orders.addAll(orderMap.values());
        return this;
    }

}
