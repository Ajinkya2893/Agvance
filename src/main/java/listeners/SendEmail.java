package listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import fileHandler.TextHandler;
import utility.Constants;


public class SendEmail extends Custom_Logger{

	public void attach(String filePath) {

		// Recipient's email ID needs to be mentioned.
		String to = Constants.SENDTO_GMAIL;

		// Sender's email ID needs to be mentioned
		String from = Constants.SENDFROM_GMAIL;

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constants.SENDFROM_GMAIL, Constants.EMAILPASSWORD_GMAIL);
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("|| BADGER || EMAIL REPORT || " + java.time.LocalDate.now().toString() + " ||");

			Multipart multipart = new MimeMultipart();

			MimeBodyPart attachmentPart = new MimeBodyPart();

			MimeBodyPart textPart = new MimeBodyPart();

			try {

				File f = new File(filePath);

				attachmentPart.attachFile(f);
				textPart.setText("Hello,\r\n" + "\r\n"
						+ "Below is the summary report of the test executed, please find the completed report in the attachment.\r\n"
						+ "\r\n" + "Thanks,\r\n" + "Ajinkya Gangawane");

				textPart.setContent(emailData(System.getProperty("user.dir") + "//test-output//emailable-report.html"),
						"text/html");
				multipart.addBodyPart(textPart);

				multipart.addBodyPart(attachmentPart);

			} catch (IOException e) {
				error(e.toString());
			}

			message.setContent(multipart);

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			error(mex.toString());
		}

	}

	public String emailData(String path) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			error(e.toString());
		}
		return contentBuilder.toString();
	}

	public void sendOutlook() {
		final String username = Constants.SENDFROM;
		final String password = Constants.EMAILPASSWORD;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Constants.SENDFROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Constants.SENDTO));
			message.setSubject("Test");
			message.setText("HI");

			MimeBodyPart attachmentPart = new MimeBodyPart();
			TextHandler TextHandler = new TextHandler();
			TextHandler.read("ExtentReport/currentLocation.txt");
			String filePath = TextHandler.readLines().get(0);
			File f = null;
			if(filePath!=null)
				f = new File(filePath);

			attachmentPart.attachFile(f);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(emailData(System.getProperty("user.dir") + "//test-output//emailable-report.html"),
					"text/html");
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(attachmentPart);

			message.setContent(multipart);

			Transport.send(message);

			System.out.println("Done");

		} catch (Exception e) {
			error(e.toString());
		}
	}
}
