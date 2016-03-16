package org.gaaroth.devlib.mail;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
	
	private String defaultFrom;
	private String username;
	private String password;
	private String smtp_host;
	private String smtp_port;

	public String getDefaultFrom() {
		return defaultFrom;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getSmtp_host() {
		return smtp_host;
	}

	public String getSmtp_port() {
		return smtp_port;
	}

	public EmailSender(Map<String, String> conf) {
		defaultFrom = conf.get("DEFAULT_FROM");
		username = conf.get("USERNAME");
		password = conf.get("PASSWORD");
		smtp_host = conf.get("SMTP_HOST");
		smtp_port = conf.get("SMTP_PORT");
	}

	public void sendEmail(EmailObject email) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		//props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtp_host);
		props.put("mail.smtp.port", smtp_port);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			if (email.getMittente() != null && !email.getMittente().isEmpty()) {
				message.setFrom(new InternetAddress(email.getMittente()));
			} else {
				message.setFrom(new InternetAddress(defaultFrom));
			}
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getDestinatario()));
			message.setSubject(email.getOggetto());
			message.setText(email.getTesto());

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}