package com.appium.pages;
 
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
 
public class Part4Page {
 
    private static final Logger logger =
            LoggerFactory.getLogger(Part4Page.class);
 
    private final AndroidDriver driver;
    private final WebDriverWait wait;
 
    private final By part4Button =
            AppiumBy.accessibilityId("Part 4");
 
    private final By section1 =
            AppiumBy.accessibilityId("Section 1");
 
    private final By section2 =
            AppiumBy.accessibilityId("Section 2");
 
    private final By section3 =
            AppiumBy.accessibilityId("Section 3");
 
    private final By submitButton =
            AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"Submit\")"
            );
 
    // Section 3 Item3 label - used to ensure the entire
    // Section 3 content has been reached by scrolling.
    private final By section3LastLabel =
            AppiumBy.xpath(
                    "//android.view.View[@content-desc='Section 3']"
                    + "/android.view.View[@content-desc='Item3']"
                    + "/following-sibling::android.widget.TextView[1]"
            );
 
    public Part4Page(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(15)
        );
    }
 
    // Open Part 4
    public void openPart4() {
 
        wait.until(
                ExpectedConditions.elementToBeClickable(part4Button)
        ).click();
 
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(section1)
        );
 
        logger.info("Part 4 opened successfully.");
    }
 
    // TAT-71: Enable all Section 1 checkboxes
    public void enableAllSection1Checkboxes() {
 
        WebElement section = wait.until(
                ExpectedConditions.visibilityOfElementLocated(section1)
        );
 
        List<WebElement> checkboxes = section.findElements(
                AppiumBy.xpath(".//*[@content-desc='checkbox']")
        );
 
        if (checkboxes.isEmpty()) {
            throw new AssertionError(
                    "No checkboxes found in Section 1."
            );
        }
 
        int selectedCount = 0;
 
        for (WebElement checkbox : checkboxes) {
 
            if (!isChecked(checkbox)) {
                checkbox.click();
            }
 
            selectedCount++;
        }
 
        logger.info(
                "Section 1: Total checkboxes enabled: {}",
                selectedCount
        );
    }
 
    // TAT-72: Enable preceding or following sibling checkboxes
    public void enableSection2SiblingCheckboxes() {
 
        WebElement section = wait.until(
                ExpectedConditions.visibilityOfElementLocated(section2)
        );
 
        List<WebElement> headings = section.findElements(
                AppiumBy.xpath(
                        ".//android.widget.TextView[contains(@text,'siblings of')]"
                )
        );
 
        if (headings.isEmpty()) {
            headings = driver.findElements(
                    By.xpath(
                            "//android.widget.TextView[contains(@text,'siblings of')]"
                    )
            );
        }
 
        if (headings.isEmpty()) {
            throw new AssertionError(
                    "Section 2 sibling heading was not found."
            );
        }
 
        String headingText = headings.get(0).getText().trim();
 
        logger.info("Section 2 heading: {}", headingText);
 
        Matcher matcher = Pattern.compile(
                "Click all (following|preceding) siblings of (Item\\d+)",
                Pattern.CASE_INSENSITIVE
        ).matcher(headingText);
 
        if (!matcher.find()) {
            throw new AssertionError(
                    "Section 2 heading could not be understood: "
                    + headingText
            );
        }
 
        String direction = matcher.group(1).toLowerCase();
        String anchor = matcher.group(2);
 
        List<WebElement> items = section.findElements(
                AppiumBy.xpath(
                        ".//*[starts-with(@content-desc,'Item')]"
                )
        );
 
        int anchorIndex = -1;
 
        for (int index = 0; index < items.size(); index++) {
 
            String itemName =
                    items.get(index).getAttribute("content-desc");
 
            if (anchor.equals(itemName)) {
                anchorIndex = index;
                break;
            }
        }
 
        if (anchorIndex == -1) {
            throw new AssertionError(
                    "Anchor item not found: " + anchor
            );
        }
 
        int selectedCount = 0;
 
        for (int index = 0; index < items.size(); index++) {
 
            boolean shouldSelect =
                    "following".equals(direction)
                    ? index > anchorIndex
                    : index < anchorIndex;
 
            if (!shouldSelect) {
                continue;
            }
 
            WebElement item = items.get(index);
 
            List<WebElement> checkboxes = item.findElements(
                    AppiumBy.accessibilityId("checkbox")
            );
 
            for (WebElement checkbox : checkboxes) {
 
                if (!isChecked(checkbox)) {
                    checkbox.click();
                }
 
                selectedCount++;
            }
        }
 
        logger.info(
                "Section 2: Anchor: {} | Direction: {} | Checkboxes enabled: {}",
                anchor, direction, selectedCount
        );
    }
 
    // TAT-73: Enable only Section 3 checkboxes with Yes labels
    public void enableSection3YesCheckboxes() {
 
        // Scroll until the last Section 3 label is visible.
        scrollToSection(section3);
 
        // Find Item1, Item2 and Item3 using inspected hierarchy.
        List<WebElement> items = wait.until(
                ExpectedConditions.numberOfElementsToBe(
                        AppiumBy.xpath(
                                "//android.view.View[@content-desc='Section 3']"
                                + "/android.view.View[starts-with(@content-desc,'Item')]"
                        ),
                        3
                )
        );
 
        logger.info(
                "Section 3: Items found: {}",
                items.size()
        );
 
        int selectedCount = 0;
 
        for (WebElement item : items) {
 
            String itemName =
                    item.getAttribute("content-desc");
 
            // Find the label following this item.
            // Use driver.findElements because the label is a sibling,
            // not a child of the Item element.
            List<WebElement> labels = driver.findElements(
                    AppiumBy.xpath(
                            "//android.view.View[@content-desc='Section 3']"
                            + "/android.view.View[@content-desc='"
                            + itemName
                            + "']/following-sibling::android.widget.TextView[1]"
                    )
            );
 
            if (labels.isEmpty()) {
                throw new AssertionError(
                        "Yes/No label not found for " + itemName
                );
            }
 
            String labelText =
                    labels.get(0).getText().trim();
 
            logger.info(
                    "Section 3: {} has label: {}",
                    itemName, labelText
            );
 
            if ("Yes".equalsIgnoreCase(labelText)) {
 
            	By checkboxLocator = AppiumBy.xpath(
            		    "//android.view.View[@content-desc='Section 3']"
            		    + "/android.view.View[@content-desc='" + itemName + "']"
            		    + "/android.widget.CheckBox[@content-desc='checkbox']"
            		);
            		 
            		List<WebElement> checkboxes =
            		        driver.findElements(checkboxLocator);
            		 
            		if (checkboxes.isEmpty()) {
            		    throw new AssertionError(
            		        "Checkbox not found for " + itemName
            		    );
            		}
            		 
            		WebElement checkbox = checkboxes.get(0);
            		 
            		if (!isChecked(checkbox)) {
            		    checkbox.click();
            		}
            		 
            		selectedCount++;
            		 
            		logger.info(
            		    "{} checkbox enabled successfully.",
            		    itemName
            		);	 
            }
        }
 
        logger.info(
                "Section 3: Total Yes checkboxes enabled: {}",
                selectedCount
        );
    }
 
    // Scroll until the last Section 3 label is fully visible.
    private void scrollToSection(By locator) {
 
        int maxAttempts = 15;
 
        int screenHeight = driver.manage()
                .window().getSize().getHeight();
 
        for (int i = 1; i <= maxAttempts; i++) {
 
            List<WebElement> labels =
                    driver.findElements(section3LastLabel);
 
            if (!labels.isEmpty()) {
 
                WebElement lastLabel = labels.get(0);
 
                org.openqa.selenium.Rectangle rect =
                        lastLabel.getRect();
 
                int labelTop = rect.getY();
                int labelBottom = rect.getY() + rect.getHeight();
 
                logger.info(
                        "Section 3 last label position: Top={}, Bottom={}, Screen={}",
                        labelTop, labelBottom, screenHeight
                );
 
                // Stop only when Item3 label is visible
                // with sufficient space at the bottom.
                if (labelTop >= 0 &&
                    labelBottom <= screenHeight - 100) {
 
                    logger.info(
                            "Section 3 fully reached after {} attempts.",
                            i - 1
                    );
 
                    return;
                }
            }
 
            logger.info(
                    "Scrolling towards complete Section 3: attempt {}",
                    i
            );
 
            slowScrollUp();
        }
 
        throw new AssertionError(
                "Section 3 last label not visible after "
                + maxAttempts + " scroll attempts."
        );
    }
 
    // Checkbox state
    private boolean isChecked(WebElement checkbox) {
 
        return checkbox.isSelected()
                || "true".equalsIgnoreCase(
                        checkbox.getAttribute("checked")
                );
    }
 
    // Scroll to Submit and click
    public void clickSubmit() {
 
        logger.info(
                "Scrolling Part 4 page to Submit button."
        );
 
        for (int i = 1; i <= 15; i++) {
 
            List<WebElement> buttons =
                    driver.findElements(submitButton);
 
            if (!buttons.isEmpty() &&
                buttons.get(0).isDisplayed()) {
 
                wait.until(
                        ExpectedConditions.elementToBeClickable(submitButton)
                ).click();
 
                logger.info(
                        "Submit button clicked successfully."
                );
                return;
            }
 
            logger.info(
                    "Part 4 scrolling attempt: {}",
                    i
            );
 
            slowScrollUp();
        }
 
        throw new AssertionError(
                "Submit button not visible after 15 scroll attempts."
        );
    }
 
    // Validate success popup
    public void validateSuccess(String expectedMessage) {
 
        logger.info(
                "Waiting for success popup: {}",
                expectedMessage
        );
 
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> d.findElements(
                        AppiumBy.className("android.widget.TextView")
                ).stream().anyMatch(element ->
                        expectedMessage.equals(element.getText().trim())
                ));
 
        logger.info(
                "Success message validated: {}",
                expectedMessage
        );
    }
 
    // Smooth scrolling method
    private void slowScrollUp() {
 
        PointerInput finger = new PointerInput(
                PointerInput.Kind.TOUCH,
                "finger"
        );
 
        Sequence swipe = new Sequence(finger, 1);
 
        swipe.addAction(
                finger.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.viewport(),
                        600, 1900
                )
        );
 
        swipe.addAction(
                finger.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );
 
        swipe.addAction(
                finger.createPointerMove(
                        Duration.ofMillis(1200),
                        PointerInput.Origin.viewport(),
                        600, 1100
                )
        );
 
        swipe.addAction(
                finger.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );
 
        driver.perform(Arrays.asList(swipe));
 
        logger.info("Part 4: Smooth scroll performed.");
    }
}

 