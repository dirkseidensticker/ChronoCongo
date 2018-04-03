package de.rgzm.alligator.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for reading the pom.xml file
 * @author thiery
 */
public class FoolibProperties {

	private static final Properties prop = new Properties();
	private static final String fileName = "foolib.properties";

	/**
     * load properties file
     * @param fileName
     * @return true if loaded
     * @throws IOException 
     */
    private static boolean loadpropertyFile(String fileName) throws IOException {
		InputStream input = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			input = classLoader.getResourceAsStream(fileName);
			prop.load(input);
			return true;
		} catch (Exception e) {
			throw new IOException(e.toString());
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	/**
     * get var of properties file
     * @param param
     * @return value of param
     * @throws IOException 
     */
    public static String getPropertyParam(String param) throws IOException {
		loadpropertyFile(fileName);
		return prop.getProperty(param);
	}

}

