package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class InvoiceRequest {

    private String deliveryId;

    private String invoiceNum;

}
