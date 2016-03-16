package org.gaaroth.devlib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkUtils {
	
	private static final Logger logger = LogManager.getLogger(NetworkUtils.class);	
	
	public static void getFileFromUrl(String urlString, String pathFileToSave) {
		URL url;
		InputStream in = null;
		OutputStream out = null;
		try {
			url = new URL(urlString);
			in = new BufferedInputStream(url.openStream());
			out = new BufferedOutputStream(new FileOutputStream(pathFileToSave));
			for ( int i; (i = in.read()) != -1; ) {
			    out.write(i);
			}
		} catch (Exception e) {
			logger.error("Errore download risorsa o salvataggio sul file system");
		} finally {
			try {
				if (in != null) in.close();
				if (out != null) out.close();
			} catch (Exception e) {}
		}
	}

}
