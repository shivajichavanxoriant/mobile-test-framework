 
package com.appium.driver;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import io.appium.java_client.android.AndroidDriver;
 
public class DriverManager {
 
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
 
    private static AndroidDriver driver;
 
    public static void setDriver(AndroidDriver driverInstance) {
        driver = driverInstance;
        logger.info("Android driver initialized successfully.");
    }
 
    public static AndroidDriver getDriver() {
        return driver;
    }
 
    public static void quitDriver() {
        if (driver != null) {
            logger.info("Closing Android driver.");
 
            driver.quit();
            driver = null;
 
            logger.info("Android driver closed successfully.");
        }
    }
}
 