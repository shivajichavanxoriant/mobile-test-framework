package com.appium.pages;
 
import java.time.Duration;
import java.util.List;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
 
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
 
public class Part4Page {
 
    private final AndroidDriver driver;
    private final WebDriverWait wait;
 
    // =====================================================
    // Part 4 locators
    // =====================================================
    private final By part4Button = AppiumBy.accessibilityId("Part 4");
    private final By submitButton = AppiumBy.accessibilityId("Submit");
 
    // =====================================================
    // Constructor
    // =====================================================
    public Part4Page(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
 
    // =====================================================
    // Open Part 4
    // =====================================================
    public void openPart4() {
        wait.until(ExpectedConditions.elementToBeClickable(part4Button)).click();
        System.out.println("Part 4 opened.");
    }
 
    // =====================================================
    // Section 1: Enable all checkboxes
    // =====================================================
    public void enableSection1Checkboxes() {
        List<WebElement> checkboxes = driver.findElements(
            AppiumBy.xpath("//android.widget.CheckBox")
        );
        for (WebElement cb : checkboxes) {
            if (!cb.getAttribute("checked").equals("true")) {
                cb.click();
            }
        }
        System.out.println("Section 1 checkboxes enabled.");
    }
 
    // =====================================================
    // Section 2: Enable all preceding siblings of Item5
    // =====================================================
    public void enableSection2Checkboxes() {
        List<WebElement> preceding = driver.findElements(
            AppiumBy.xpath("//android.widget.CheckBox[@text='Item5']/preceding-sibling::android.widget.CheckBox")
        );
        for (WebElement cb : preceding) {
            if (!cb.getAttribute("checked").equals("true")) {
                cb.click();
            }
        }
        System.out.println("Section 2 checkboxes enabled.");
    }
 
    // =====================================================
    // Section 3: Enable only checkboxes with 'Yes' under them
    // =====================================================
    public void enableSection3Checkboxes() {
        List<WebElement> yesCheckboxes = driver.findElements(
            AppiumBy.xpath("//android.widget.CheckBox[following-sibling::android.widget.TextView[@text='Yes']]")
        );
        for (WebElement cb : yesCheckboxes) {
            if (!cb.getAttribute("checked").equals("true")) {
                cb.click();
            }
        }
        System.out.println("Section 3 checkboxes enabled.");
    }
 
    // =====================================================
    // Click Submit
    // =====================================================
    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        System.out.println("Submit clicked.");
    }
 
    // =====================================================
    // Validate Success popup
    // =====================================================
    public boolean validateSuccess(String section) {
        WebElement popup = wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.xpath("//android.widget.TextView[@text='Success: " + section + "']")
        ));
        return popup.isDisplayed();
    }
}
 