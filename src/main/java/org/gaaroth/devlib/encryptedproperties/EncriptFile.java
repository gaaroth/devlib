package org.gaaroth.devlib.encryptedproperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EncriptFile {

	public static void main(String[] args) throws Exception {
		//TODO .properties generator
		File file = new File("C:\\database.properties");
		if (!file.exists()) {
			file.createNewFile();
		}
		EncryptedProperties ep = new EncryptedProperties("cruff");
		
		FileInputStream in = new FileInputStream(file);
		ep.load(in);
		
		ep.setProperty("jdbcDriver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		ep.setProperty("databaseType", "sqlserver");
		ep.setProperty("uri", "srv-dotnet\\sqlexpress");
		ep.setProperty("port", "");
		ep.setProperty("databaseName", "magentosync");
		ep.setProperty("username", "sa");
		ep.setProperty("password", "Admin000");
		
		FileOutputStream out = new FileOutputStream(file);
		ep.store(out, "Encrypted database properties file");
		out.close();
	}

}
