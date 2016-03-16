package org.gaaroth.devlib.database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.gaaroth.devlib.encryptedproperties.EncryptedProperties;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DatabaseUtils {

	//ENTITY PRIVATE PROPS
//	private static final String TRANSACTION_TYPE = "";
	private static final String JDBC_DRIVER = "javax.persistence.jdbc.driver";
	private static final String JDBC_URL = "javax.persistence.jdbc.url";
	private static final String JDBC_USER = "javax.persistence.jdbc.user";
	private static final String JDBC_PASSWORD = "javax.persistence.jdbc.password";
	private static final String LOGGING_LEVEL = "eclipselink.logging.level";
//	private static final String LOGGING_TIMESTAMP = "";
//	private static final String LOGGING_THREAD = "";
//	private static final String LOGGING_SESSION = "";
//	private static final String TARGET_SERVER = "";
	
	//MAP PUBLIC CUSTOM PROPS
	public static final String INPUT_JDBC_DRIVER = "jdbcDriver";
	public static final String INPUT_DATABASE_TYPE = "databaseType";
	public static final String INPUT_URI = "uri";
	public static final String INPUT_PORT = "port";
	public static final String INPUT_DATABASE_NAME = "databaseName";
	public static final String INPUT_USERNAME = "username";
	public static final String INPUT_PASSWORD = "password";
	
	public static EntityManager createEntityManager(String persistenceUnitName, Map<String, String> properties) {
		return Persistence.createEntityManagerFactory(persistenceUnitName, properties).createEntityManager();
	}
	
	public static EntityManager createEntityManager(String persistenceUnitName, String pathProperties) {
		return createEntityManager(persistenceUnitName, createEntityManagerProperties(pathProperties, null));
	}
	
	/**
	 * Definire input File o da Map, se esistono properties nella seconda queste vincono sul primo
	 */
	public static Map<String, String> createEntityManagerProperties(String pathProperties, Map<String, String> customProps) {		
		String jdbcDriver = null;
		String databaseType = null;
		String uri = null;
		String port = null;
		String databaseName = null;
		String username = null;
		String password = null;
		String url = null;
		try {
			
			if (customProps != null) {
				jdbcDriver = customProps.get(INPUT_JDBC_DRIVER);
				databaseType = customProps.get(INPUT_DATABASE_TYPE);
				uri = customProps.get(INPUT_URI);
				port = customProps.get(INPUT_PORT);
				databaseName = customProps.get(INPUT_DATABASE_NAME);
				username = customProps.get(INPUT_USERNAME);
				password = customProps.get(INPUT_PASSWORD);
			}
			
			if (pathProperties != null) {
				EncryptedProperties ep;
				ep = new EncryptedProperties("cruff");
				FileInputStream in = new FileInputStream(pathProperties);
				ep.load(in);
				if (jdbcDriver == null) {
					jdbcDriver = ep.getProperty("jdbcDriver");
				}
				if (databaseType == null) {
					databaseType = ep.getProperty("databaseType");
				}
				if (uri == null) {
					uri = ep.getProperty("uri");
				}
				if (port == null) {
					port = ep.getProperty("port");
				}
				if (databaseName == null) {
					databaseName = ep.getProperty("databaseName");
				}
				if (username == null) {
					username = ep.getProperty("username");
				}
				if (password == null) {
					password = ep.getProperty("password");
				}
			}

			if ("sqlserver".equals(databaseType)) {
				url = "jdbc:"+databaseType+"://"+uri+(port != null && !port.isEmpty() ? ";port="+port : "")+";databaseName="+databaseName+";";
			} else if ("mysql".equals(databaseType)) {
				url = "jdbc:"+databaseType+"://"+uri+(port != null && !port.isEmpty() ? ":"+port : "")+"/"+databaseName;
			}
		
			Map<String, String> properties = new HashMap<String, String>();
			 
		    // Ensure RESOURCE_LOCAL transactions is used.
//		    properties.put(TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
		 
		    // Configure the internal EclipseLink connection pool
		    properties.put(JDBC_DRIVER, jdbcDriver);
		    properties.put(JDBC_URL, url);
		    properties.put(JDBC_USER, username);
		    properties.put(JDBC_PASSWORD, password);
		 
		    // Configure logging. FINE ensures all SQL is shown
		    properties.put(LOGGING_LEVEL, "FINE");
//		    properties.put(LOGGING_TIMESTAMP, "false");
//		    properties.put(LOGGING_THREAD, "false");
//		    properties.put(LOGGING_SESSION, "false");
		 
		    // Ensure that no server-platform is configured
//		    properties.put(TARGET_SERVER, TargetServer.None);
	
		    return properties;		
		} catch (Exception e) {
			throw new RuntimeException("Errore creazione DatabaseManager", e);
		}
	}
	
	public static SimpleJDBCConnectionPool getConnectionPool(String pathProperties) {
		return getConnectionPool(createEntityManagerProperties(pathProperties, null));
	}
	
	public static SimpleJDBCConnectionPool getConnectionPool(String pathProperties, Map<String, String> customProps) {
		return getConnectionPool(createEntityManagerProperties(pathProperties, customProps));
	}
	
	public static SimpleJDBCConnectionPool getConnectionPool(Map<String, String> properties) {
		try {
			SimpleJDBCConnectionPool simpleJDBCConnectionPool = new SimpleJDBCConnectionPool(properties.get(JDBC_DRIVER), properties.get(JDBC_URL), properties.get(JDBC_USER), properties.get(JDBC_PASSWORD));
			return simpleJDBCConnectionPool;
		} catch (Exception e) {
			throw new RuntimeException("Errore creazione pool JDBC");
		}
	}
	
	public static Connection getConnection(String pathProperties) {
		return getConnection(createEntityManagerProperties(pathProperties, null));		
	}
	
	public static Connection getConnection(String pathProperties, Map<String, String> customProps) {
		return getConnection(createEntityManagerProperties(pathProperties, customProps));
	}
	
	public static Connection getConnection(Map<String, String> properties) {
		try {
			Class.forName(properties.get(JDBC_DRIVER));
			Connection connection = DriverManager.getConnection(properties.get(JDBC_URL), properties.get(JDBC_USER), properties.get(JDBC_PASSWORD));
			return connection;
		} catch (Exception e) {
			throw new RuntimeException("Errore connessione database");
		}
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Errore chiusura connessione");
		}
	}

}
