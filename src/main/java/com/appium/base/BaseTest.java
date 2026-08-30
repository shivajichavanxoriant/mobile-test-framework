package com.appium.base;
 
import java.net.URL;
 
import com.appium.config.ConfigReader;
import com.appium.driver.DriverManager;
 
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
 
public class BaseTest {
 
	public void setUp() throws Exception {
 
        ConfigReader.loadProperties("src/test/resources/config/config.properties");
 
        UiAutomator2Options options = new UiAutomator2Options();
 
        options.setPlatformName(ConfigReader.getProperty("platformName"));
        options.setAutomationName(ConfigReader.getProperty("automationName"));
        options.setDeviceName(ConfigReader.getProperty("deviceName"));
        options.setApp(ConfigReader.getProperty("appPath"));
 
        AndroidDriver driver = new AndroidDriver(
        		new URL(ConfigReader.getProperty("appiumServer")),
                options);
 
        DriverManager.setDriver(driver);
    }
 
    public void tearDown() {
 
        DriverManager.quitDriver();
    }
}
