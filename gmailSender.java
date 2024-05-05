package GmailReading;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;
public class gmailSender {
    public static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from)); //me
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to)); //
        email.setSubject(subject);

        email.setText(bodyText);

        return email;
    }
    public static String getRandom(int len) {
        StringBuilder sb = new StringBuilder();
        String character = "1234567890";
        for (int i = 0; i < len; i++) {
            int index = (int) (Math.random() * character.length());
            sb.append(character.charAt(index));
        }
        return sb.toString();
    }
    public static void sendEmail() throws IOException, GeneralSecurityException, MessagingException {

        Gmail service = GmailAPI.getService();
        String subject = "Test QA Mail " + getRandom(4);
        MimeMessage Mimemessage = createEmail("diksham613@gmail.com","me",subject,"Body Content is Empty");

        Message message = createMessageWithEmail(Mimemessage);

        message = service.users().messages().send("me", message).execute();
        System.out.println(subject);
        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
    }



    public static void main(String[] args) throws IOException, GeneralSecurityException, MessagingException, InterruptedException {
        int i = 0;
        while (i<30) {
            sendEmail();
            i++;
        }
    }
}
