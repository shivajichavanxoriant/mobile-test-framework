 
package com.appium.hooks;
 
import com.appium.base.AppiumServerManager;
import com.appium.base.BaseTest;
import com.appium.driver.DriverManager;
 
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
 
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.ByteArrayInputStream;
 
public class Hooks {
 
    private static final Logger logger =
            LoggerFactory.getLogger(Hooks.class);
 
    private static final BaseTest baseTest = new BaseTest();
 
    @BeforeAll
    public static void startAppiumServer() throws Exception {
        logger.info("Starting Appium server.");
        AppiumServerManager.startServer();
    }
 
    @Before
    public void setUp() throws Exception {
        logger.info("Initializing test scenario.");
        baseTest.setUp();
    }
 
    @After
    public void tearDown(Scenario scenario) {
 
        try {
            if (scenario.isFailed()) {
 
                logger.warn(
                        "Scenario failed: {}. Capturing screenshot.",
                        scenario.getName()
                );
 
                AndroidDriver driver = DriverManager.getDriver();
 
                if (driver != null) {
 
                    byte[] screenshot =
                            driver.getScreenshotAs(OutputType.BYTES);
 
                    Allure.attachment(
                            "Failure Screenshot - "
                                    + scenario.getName() + ".png",
                            new ByteArrayInputStream(screenshot)
                    );
 
                    logger.info(
                            "Screenshot attached to Allure for scenario: {}",
                            scenario.getName()
                    );
                }
            }
 
        } catch (Exception e) {
 
            logger.error(
                    "Unable to capture failure screenshot for scenario: {}",
                    scenario.getName(),
                    e
            );
 
        } finally {
            baseTest.tearDown();
            logger.info(
                    "Test scenario cleanup completed: {}",
                    scenario.getName()
            );
        }
    }
 
    @AfterAll
    public static void stopAppiumServer() {
        logger.info("Stopping Appium server.");
        AppiumServerManager.stopServer();
    }
}
 