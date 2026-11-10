 
package com.appium.stepdefinitions;
 
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import org.testng.Assert;
 
import com.appium.driver.DriverManager;
import com.appium.pages.Part3Page;
import com.appium.pages.Part3Page.ProductData;
 
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
public class Part3Steps {
 
    private Part3Page part3Page;
 
    private List<ProductData> products;
 
    private double displayedTotal;
 
    private double calculatedTotal;
 
    private Map<String, Double> calculatedBrandTotals =
            new LinkedHashMap<>();
 
    private Map<String, Double> displayedBrandTotals =
            new LinkedHashMap<>();
 
    private Set<String> imageNames;
 
    // --------------------------------------------------
    // TAT-68: OPEN PART 3
    // --------------------------------------------------
 
    @When("I open Part 3")
    public void iOpenPart3() {
 
        initializePart3();
 
        displayedTotal = part3Page.getDisplayedTotal();
    }
 
    // --------------------------------------------------
    // TAT-68: SCROLL AND COLLECT PRODUCTS
    // --------------------------------------------------
 
    @And("I scroll through the product list")
    public void iScrollThroughTheProductList() {
 
        products = part3Page.getAllProducts();
 
        Assert.assertFalse(
                products.isEmpty(),
                "No products were collected."
        );
 
        calculatedTotal = 0.0;
 
        for (ProductData product : products) {
 
            calculatedTotal += product.getPrice();
        }
 
        System.out.println(
                "Calculated product total: " + calculatedTotal
        );
 
        System.out.println(
                "Displayed product total: " + displayedTotal
        );
    }
 
    // --------------------------------------------------
    // TAT-68: VALIDATE TOTAL
    // --------------------------------------------------
 
    @Then("the total should match the sum of all item prices")
    public void theTotalShouldMatchTheSumOfAllItemPrices() {
 
        Assert.assertEquals(
                calculatedTotal,
                displayedTotal,
                0.01,
                "Product price sum does not match displayed Total."
        );
 
        System.out.println("TAT-68 passed.");
    }
 
    // --------------------------------------------------
    // TAT-69: OPEN PART 3
    // --------------------------------------------------
 
    @When("I open Part 3 for brand total validation")
    public void iOpenPart3ForBrandTotalValidation() {
 
        initializePart3();
 
        // Capture displayed brand totals before scrolling.
        displayedBrandTotals =
                part3Page.getDisplayedBrandTotals();
 
        System.out.println(
                "Displayed brand totals captured: "
                        + displayedBrandTotals
        );
    }
 
    // --------------------------------------------------
    // TAT-69: CALCULATE BRAND TOTALS
    // --------------------------------------------------
 
    @And("I scroll through the product list and calculate brand totals")
    public void iScrollAndCalculateBrandTotals() {
 
        // Existing product collection remains unchanged.
        products = part3Page.getAllProducts();
 
        calculatedBrandTotals.clear();
 
        for (ProductData product : products) {
 
            calculatedBrandTotals.merge(
                    product.getBrand(),
                    product.getPrice(),
                    Double::sum
            );
        }
 
        System.out.println(
                "Calculated brand totals: "
                        + calculatedBrandTotals
        );
 
        System.out.println(
                "Displayed brand totals: "
                        + displayedBrandTotals
        );
    }
 
    // --------------------------------------------------
    // TAT-69: VALIDATE BRAND TOTALS
    // --------------------------------------------------
 
    @Then("the displayed brand totals should match the calculated brand totals")
    public void displayedBrandTotalsShouldMatchCalculatedBrandTotals() {
 
        Assert.assertFalse(
                calculatedBrandTotals.isEmpty(),
                "No calculated brand totals were found."
        );
 
        Assert.assertFalse(
                displayedBrandTotals.isEmpty(),
                "No displayed brand totals were collected."
        );
 
        Assert.assertEquals(
                displayedBrandTotals.size(),
                calculatedBrandTotals.size(),
                "Number of displayed brands does not match calculated brands."
        );
 
        for (String brand : calculatedBrandTotals.keySet()) {
 
            Assert.assertTrue(
                    displayedBrandTotals.containsKey(brand),
                    "Displayed total missing for brand: " + brand
            );
 
            double calculated =
                    calculatedBrandTotals.get(brand);
 
            double displayed =
                    displayedBrandTotals.get(brand);
 
            Assert.assertEquals(
                    calculated,
                    displayed,
                    0.01,
                    "Brand total mismatch for: " + brand
            );
        }
 
        System.out.println("TAT-69 passed.");
    }
 
    // --------------------------------------------------
    // TAT-70: OPEN PART 3
    // --------------------------------------------------
 
    @When("I open Part 3 for image validation")
    public void iOpenPart3ForImageValidation() {
 
        initializePart3();
    }
 
    // --------------------------------------------------
    // TAT-70: COLLECT IMAGE NAMES
    // --------------------------------------------------
 
    @And("I scroll through the product list and save all item image names")
    public void iScrollAndSaveAllItemImageNames() {
 
        products = part3Page.getAllProducts();
 
        imageNames = part3Page.getCollectedImageNames();
 
        System.out.println(
                "Collected unique image names: " + imageNames
        );
    }
 
    // --------------------------------------------------
    // TAT-70: VALIDATE IMAGE NAMES
    // --------------------------------------------------
 
    @Then("all item image names should match the allowed image names")
    public void allItemImageNamesShouldMatchTheAllowedImageNames() {
 
        Assert.assertNotNull(
                imageNames,
                "Image names collection is null."
        );
 
        Assert.assertFalse(
                imageNames.isEmpty(),
                "No image names were collected."
        );
 
        for (String imageName : imageNames) {
 
            Assert.assertTrue(
                    imageName.matches("^FAKEIMAGE-.+"),
                    "Unexpected image name: " + imageName
            );
        }
 
        System.out.println("TAT-70 passed.");
    }
 
    // --------------------------------------------------
    // INITIALIZE PART 3
    // --------------------------------------------------
 
    private void initializePart3() {
 
        part3Page = new Part3Page(
                DriverManager.getDriver()
        );
 
        part3Page.openPart3();
    }
}
 