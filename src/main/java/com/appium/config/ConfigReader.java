package com.appium.config;
 
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
 
public class ConfigReader {
 
    private static Properties properties = new Properties();
 
    public static void loadProperties(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        properties.load(fileInputStream);
    }
 
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}