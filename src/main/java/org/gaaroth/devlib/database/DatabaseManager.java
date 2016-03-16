package org.gaaroth.devlib.database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gaaroth.devlib.encryptedproperties.EncryptedProperties;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DatabaseManager {
	
	private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
	
	private String jdbcDriver;
	private String databaseType;
	private String uri;
	private String port;
	private String databaseName;
	private String username;
	private String password;
	private String url;
	
	public DatabaseManager(String propertiesFilePath) {
		EncryptedProperties ep;
		try {
			ep = new EncryptedProperties("cruff");
			FileInputStream in = new FileInputStream(propertiesFilePath);//getFilePath()
			ep.load(in);
			jdbcDriver = ep.getProperty("jdbcDriver");
			databaseType = ep.getProperty("databaseType");
			uri = ep.getProperty("uri");
			port = ep.getProperty("port");
			databaseName = ep.getProperty("databaseName");
			username = ep.getProperty("username");
			password = ep.getProperty("password");
			url = "jdbc:"+databaseType+"://"+uri+(port != null && !port.isEmpty() ? ";port="+port : "")+";databaseName="+databaseName+";";
		} catch (Exception e) {
			logger.error("Errore creazione DatabaseManager", e);
			throw new RuntimeException("Errore creazione DatabaseManager");
		}
	}
	
	public SimpleJDBCConnectionPool getConnectionPool() {
		try {
			SimpleJDBCConnectionPool simpleJDBCConnectionPool = new SimpleJDBCConnectionPool(jdbcDriver, url, username, password);
			return simpleJDBCConnectionPool;
		} catch (Exception e) {
			logger.error("Errore creazione pool JDBC", e);
			throw new RuntimeException("Errore creazione pool JDBC");
		}
	}
	
	public Connection getConnection() {
		try {
			Class.forName(jdbcDriver);
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (Exception e) {
			logger.error("Errore connessione database", e);
			throw new RuntimeException("Errore connessione database");
		}
	}

	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			logger.error("Errore chiusura connessione database", e);
		}
	}

}
