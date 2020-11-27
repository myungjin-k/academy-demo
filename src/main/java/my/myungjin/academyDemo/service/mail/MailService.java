package my.myungjin.academyDemo.service.mail;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.mail.Mail;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final String FROM_ADDRESS = "open7894@gmail.com";

    public void sendMail(Mail mail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mail.getTitle());
        message.setText(mail.getContent());

        javaMailSender.send(message);
    }
}
