package laborator2;

import javax.mail.*;
import java.io.IOException;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.internet.*;

public class App {
    public static void main(String[] args) throws IOException, MessagingException {
        final String username = "angiestavnk@gmail.com";
        final String password = "*******";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("ackzackz@gmail.com")
            );
            message.setSubject("Testing Gmail TLS");
            message.setText("HELLOOOOOOOOOOOOOO");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        ReceiveMailImap.saveMesages();
    }
}
