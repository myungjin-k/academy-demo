package my.myungjin.academyDemo.service.mail;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final SpringTemplateEngine thymeleafTemplateEngine;

    public void sendMessageUsingThymeleafTemplate(String to, String subject, String templateFileName, Map<String, Object> templateModel)
            throws MessagingException, UnsupportedEncodingException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(templateFileName, thymeleafContext);

        Mail mail = Mail.builder()
                .to(to)
                .title(subject)
                .variables(templateModel)
                .htmlBody(htmlBody)
                .build();

        sendMail(mail);
    }

    public void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getTitle());
        helper.setText(mail.getHtmlBody(), true);
        mailSender.send(message);
    }
}
