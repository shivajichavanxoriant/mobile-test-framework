package com.appium.pages;
 
import java.time.Duration;
 
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
 
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
 
public class LoginPage {
	 
    private WebDriverWait wait;
 
    public LoginPage(AndroidDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
 
    // Locators
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
 
    private By validationMessage =
            AppiumBy.xpath(
                    "//android.widget.TextView[contains(@text,'Incorrect username!')]"
            );
 
    private By successMessage =
            AppiumBy.xpath(
                    "//android.widget.TextView[@text='Success']"
            );
 
    private By successOkButton =
            AppiumBy.xpath("//*[@text='Ok' or @text='OK']");
 
    // Actions
    public void openPart1() {
        wait.until(
                ExpectedConditions.elementToBeClickable(part1Button)
        ).click();
    }
 
    public void enterUserName(String username) {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(userNameField)
        ).sendKeys(username);
    }
 
    public void enterEmail(String email) {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(emailField)
        ).sendKeys(email);
    }
 
    public void enterPassword(String password) {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField)
        ).sendKeys(password);
    }
 
    public void clickSubmit() {
        wait.until(
                ExpectedConditions.elementToBeClickable(submitButton)
        ).click();
    }
 
    // Validation methods
    public boolean isValidationMessageDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(validationMessage)
        ).isDisplayed();
    }
 
    public boolean isSuccessMessageDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage)
        ).isDisplayed();
    }
 
    public void clickSuccessOkButton() {
        wait.until(
                ExpectedConditions.elementToBeClickable(successOkButton)
        ).click();
    }
}