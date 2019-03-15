package util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static PropertiesUtil instance = null;
    private Properties properties;

    private PropertiesUtil() {
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new PropertiesUtil();
        }
        return instance;
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
