package my.myungjin.academyDemo.service.mail;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.mail.Mail;
import my.myungjin.academyDemo.commons.mail.MailHandler;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Transactional
    public void sendMail(Mail mail) throws MessagingException {
        String FROM = "open7894@gmail.com";
        MailHandler mailHandler = new MailHandler(javaMailSender);
        mailHandler.setTo(mail.getTo());
        mailHandler.setFrom(FROM);
        mailHandler.setSubject(mail.getTitle());
        mailHandler.setContent(mail.getContent());

        mailHandler.send();
    }
}
