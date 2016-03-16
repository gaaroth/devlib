package org.gaaroth.devlib.mail;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.gaaroth.devlib.encryptedproperties.EncryptedProperties;

public class EmailSenderUtils {
	
	private final static String DEFAULT_FROM = "DEFAULT_FROM";
	private final static String USERNAME = "USERNAME";
	private final static String PASSWORD = "PASSWORD";
	private final static String SMTP_HOST = "SMTP_HOST";
	private final static String SMTP_PORT = "SMTP_PORT";
	

	public static Map<String, String> createEmailProperties(String pathProperties, Map<String, String> customProps) {
		String default_from = "dev@cremonaufficio.com";
		String username = "dev@cremonaufficio.com";
		String password = "pippo00";
		String smtp_host = "cremonaufficio.com";
		String smtp_port = "25";
		
		try {
			
			if (customProps != null) {
				default_from = customProps.get(DEFAULT_FROM);
				username = customProps.get(USERNAME);
				password = customProps.get(PASSWORD);
				smtp_host = customProps.get(SMTP_HOST);
				smtp_port = customProps.get(SMTP_PORT);
			}
			
			if (pathProperties != null) {
				EncryptedProperties ep;
				ep = new EncryptedProperties("cruff");
				FileInputStream in = new FileInputStream(pathProperties);
				ep.load(in);
				if (default_from == null) {
					default_from = ep.getProperty("default_from");
				}
				if (username == null) {
					username = ep.getProperty("username");
				}
				if (password == null) {
					password = ep.getProperty("password");
				}
				if (smtp_host == null) {
					smtp_host = ep.getProperty("smtp_host");
				}
				if (smtp_port == null) {
					smtp_port = ep.getProperty("smtp_port");
				}
			}

			Map<String, String> properties = new HashMap<String, String>();
			properties.put(DEFAULT_FROM, default_from);
		    properties.put(USERNAME, username);
		    properties.put(PASSWORD, password);
		    properties.put(SMTP_HOST, smtp_host);
		    properties.put(SMTP_PORT, smtp_port);
		    return properties;		
		} catch (Exception e) {
			throw new RuntimeException("Errore creazione properties email", e);
		}
	}

}
