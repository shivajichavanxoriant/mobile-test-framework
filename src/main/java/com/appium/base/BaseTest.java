 
package com.appium.base;
 
import java.net.URI;
 
import com.appium.config.ConfigReader;
import com.appium.constants.FrameworkConstants;
import com.appium.driver.DriverManager;
 
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class BaseTest {
 
    private static final Logger logger =
            LoggerFactory.getLogger(BaseTest.class);
 
    public void setUp() throws Exception {
 
        try {
            logger.info("Loading framework configuration");
 
            ConfigReader.loadProperties(
                    FrameworkConstants.CONFIG_FILE_PATH);
 
            UiAutomator2Options options = new UiAutomator2Options();
 
            options.setPlatformName(
                    ConfigReader.getProperty("platformName"));
 
            options.setAutomationName(
                    ConfigReader.getProperty("automationName"));
 
            options.setDeviceName(
                    ConfigReader.getProperty("deviceName"));
 
            options.setApp(
                    ConfigReader.getProperty("appPath"));
 
            String appiumServerUrl =
                    ConfigReader.getProperty("appiumServer");
 
            logger.info("Initializing Android Appium driver");
 
            AndroidDriver driver = new AndroidDriver(
                    URI.create(appiumServerUrl).toURL(),
                    options);
 
            DriverManager.setDriver(driver);
 
            logger.info("Android Appium driver initialized successfully");
 
        } catch (Exception e) {
            logger.error("Failed to initialize Android Appium driver", e);
            throw e;
        }
    }
 
    public void tearDown() {
 
        logger.info("Closing Android Appium driver");
 
        DriverManager.quitDriver();
 
        logger.info("Android Appium driver closed successfully");
    }
}
 