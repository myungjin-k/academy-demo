package my.myungjin.academyDemo.service.mail;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.mail.Mail;
import my.myungjin.academyDemo.util.MailHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@ConfigurationProperties(prefix = "mail")
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private String fromAddress;

    public void sendMail(Mail mail) throws MessagingException {
        MailHandler mailHandler = new MailHandler(javaMailSender);
        mailHandler.setTo(mail.getAddress());
        mailHandler.setFrom(fromAddress);
        mailHandler.setSubject(mail.getTitle());
        mailHandler.setContent(mail.getContent());

        mailHandler.send();
    }
}
