 
package com.appium.stepdefinitions;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
 
import com.appium.driver.DriverManager;
import com.appium.pages.LoginPage;
 
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
public class LoginSteps {
 
    private static final Logger logger =
            LoggerFactory.getLogger(LoginSteps.class);
 
    private LoginPage loginPage =
            new LoginPage(DriverManager.getDriver());
 
    @Given("User opens Part 1 screen")
    public void userOpensPart1Screen() {
 
        logger.info("Opening Part 1 screen.");
 
        loginPage.openPart1();
    }
 
    @When("User enters a {string} username")
    public void userEntersUsername(String usernameType) {
 
        logger.info("Entering {} username.", usernameType);
 
        if ("valid".equalsIgnoreCase(usernameType)) {
 
            loginPage.enterUserName("MagicBird123");
 
        } else if ("invalid".equalsIgnoreCase(usernameType)) {
 
            loginPage.enterUserName("abc");
 
        } else {
 
            logger.error("Unknown username type: {}", usernameType);
            Assert.fail("Unknown username type: " + usernameType);
        }
    }
 
    @When("User enters a {string} email")
    public void userEntersEmail(String emailType) {
 
        logger.info("Entering {} email.", emailType);
 
        if ("valid".equalsIgnoreCase(emailType)) {
 
            loginPage.enterEmail("name.surname@gmail.com");
 
        } else if ("invalid".equalsIgnoreCase(emailType)) {
 
            loginPage.enterEmail("wrongemail");
 
        } else {
 
            logger.error("Unknown email type: {}", emailType);
            Assert.fail("Unknown email type: " + emailType);
        }
    }
 
    @When("User enters a {string} password")
    public void userEntersPassword(String passwordType) {
 
        logger.info("Entering {} password.", passwordType);
 
        if ("valid".equalsIgnoreCase(passwordType)) {
 
            loginPage.enterPassword("Password123!");
 
        } else if ("invalid".equalsIgnoreCase(passwordType)) {
 
            loginPage.enterPassword("wrong");
 
        } else {
 
            logger.error("Unknown password type: {}", passwordType);
            Assert.fail("Unknown password type: " + passwordType);
        }
    }
 
    @When("User taps the Submit button")
    public void userTapsSubmitButton() {
 
        logger.info("Tapping the Submit button.");
 
        loginPage.clickSubmit();
    }
 
    @Then("{string} message should be displayed")
    public void messageShouldBeDisplayed(String expectedResult) {
 
        logger.info("Validating expected result: {}", expectedResult);
 
        if ("Success".equalsIgnoreCase(expectedResult)) {
 
            Assert.assertTrue(
                    loginPage.isSuccessMessageDisplayed(),
                    "Success popup was not displayed."
            );
 
            loginPage.clickSuccessOkButton();
 
            logger.info("Success message validated.");
 
        } else if ("Validation error".equalsIgnoreCase(expectedResult)) {
 
            Assert.assertTrue(
                    loginPage.isValidationMessageDisplayed(),
                    "Validation error message was not displayed."
            );
 
            logger.info("Validation error message verified.");
 
        } else {
 
            logger.error("Unknown expected result: {}", expectedResult);
            Assert.fail("Unknown expected result: " + expectedResult);
        }
    }
}
 