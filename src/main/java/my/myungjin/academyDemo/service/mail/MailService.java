package my.myungjin.academyDemo.service.mail;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.mail.Mail;
import my.myungjin.academyDemo.configure.MailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@RequiredArgsConstructor
public class MailService {

    static final String FROM = "admin@mesmerizin.com";

    static final String FROMNAME = "mesmerizin";

    private final MailConfig mailConfig;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public void sendMail(Mail mail) throws MessagingException, UnsupportedEncodingException {

        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", mailConfig.getPort());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM,FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
        msg.setSubject(mail.getTitle());
        msg.setContent(mail.getContent(),"text/html; charset=UTF-8");

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(mailConfig.getHost(), mailConfig.getUsername(), mailConfig.getPassword());

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("Email Sent");
        } catch (Exception ex) {
            log.warn("Email send Exception : {}", ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
}
