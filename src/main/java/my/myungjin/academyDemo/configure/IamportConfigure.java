package my.myungjin.academyDemo.configure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = "payment.iamport")
@Component
public class IamportConfigure {

    private String apiKey;

    private String apiSecret;

}
