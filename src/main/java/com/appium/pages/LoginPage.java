package com.appium.pages;
 
import org.openqa.selenium.By;
 
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
 
public class LoginPage {
 
    private AndroidDriver driver;
 
    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
    }
 
    // =========================
    // Locators
    // =========================
 
    private By part1Button =
            AppiumBy.accessibilityId("Part 1");
 
    private By userNameField =
            AppiumBy.accessibilityId("User name input field");
 
    private By emailField =
            AppiumBy.accessibilityId("Email input field");
 
    private By passwordField =
            AppiumBy.accessibilityId("Password input field");
 
    private By submitButton =
            AppiumBy.accessibilityId("Submit");
 
    /*
     * Validation dialog message.
     *
     * The application displays:
     * "Incorrect username! Expected to match: ..."
     *
     * We check only "Incorrect username!" because
     * the remaining text contains the complete regex.
     */
    private By validationMessage =
            AppiumBy.xpath(
                    "//android.widget.TextView[contains(@text,'Incorrect username!')]"
            );
 
    // =========================
    // Actions
    // =========================
 
    public void openPart1() {
 
        driver.findElement(part1Button).click();
    }
 
    public void enterUserName(String username) {
 
        driver.findElement(userNameField).sendKeys(username);
    }
 
    public void enterEmail(String email) {
 
        driver.findElement(emailField).sendKeys(email);
    }
 
    public void enterPassword(String password) {
 
        driver.findElement(passwordField).sendKeys(password);
    }
 
    public void clickSubmit() {
 
        driver.findElement(submitButton).click();
    }
 
    // =========================
    // Validation
    // =========================
 
    public boolean isValidationMessageDisplayed() {
 
        return driver.findElement(validationMessage).isDisplayed();
    }
}
 