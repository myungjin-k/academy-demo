package my.myungjin.academyDemo.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MailHandler {
    private final JavaMailSender mailSender;
    private final MimeMessage message;
    private final MimeMessageHelper messageHelper;

    public MailHandler(JavaMailSender mailSender) throws MessagingException {
        this.mailSender = mailSender;
        this.message = mailSender.createMimeMessage();
        this.messageHelper = new MimeMessageHelper(this.message, false, "UTF-8");
    }

    public void setFrom(String fromAddress) throws MessagingException {
        messageHelper.setFrom(fromAddress);
    }

    public void setTo(String toAddress) throws MessagingException {
        messageHelper.setTo(toAddress);
    }

    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    public void setContent(String content) throws MessagingException {
        messageHelper.setText(content, true);
    }

    // TODO 첨부 파일, 이미지 삽입

    public void send(){
        mailSender.send(message);
    }
}
