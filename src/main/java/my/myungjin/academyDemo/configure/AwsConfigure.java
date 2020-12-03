package my.myungjin.academyDemo.configure;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Setter
@Getter
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Component
public class AwsConfigure {

    private String accessKey;

    private String secretKey;

    private String region;

    private String url;

    private String bucketName;

}
