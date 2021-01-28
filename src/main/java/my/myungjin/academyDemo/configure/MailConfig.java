package my.myungjin.academyDemo.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfig {

    private String host;

    private String port;

    private String username;

    private String password;

}
