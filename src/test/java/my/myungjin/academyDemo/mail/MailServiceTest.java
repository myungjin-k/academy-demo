package my.myungjin.academyDemo.mail;

import my.myungjin.academyDemo.commons.mail.Mail;
import my.myungjin.academyDemo.service.mail.MailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    private String to;

    private String subject;

    @BeforeAll
    void setup(){
        to = "open7894.v2@gmail.com";
        subject = "이메일 테스트";
    }

    @Test
     void 메일을_전송한다() throws Exception {
        Mail mail = Mail.builder()
                .to(to)
                .title(subject)
                .htmlBody("이메일 테스트 본문")
                .build();
        mailService.sendMail(mail);
    }

    @Test
    void 타임리프_템플릿으로_메일을_전송한다() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("test", "testVar");
        mailService.sendMessageUsingThymeleafTemplate(to, subject, "test", map);
    }
}
