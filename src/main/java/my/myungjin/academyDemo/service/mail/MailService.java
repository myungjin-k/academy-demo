package my.myungjin.academyDemo.service.mail;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.mail.Mail;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@ConfigurationProperties(prefix = "spring.mail")
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private String fromAddress;

    public void sendMail(Mail mail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(fromAddress);
        message.setSubject(mail.getTitle());
        message.setText(mail.getContent());

        javaMailSender.send(message);
    }
}
