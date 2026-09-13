package com.appium.stepdefinitions;
 
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
 
import java.math.BigDecimal;
import java.util.List;
 
import com.appium.driver.DriverManager;
import com.appium.pages.Part3Page;
import com.appium.pages.Part3Page.ProductData;
 
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
public class Part3Steps {
 
    private Part3Page part3Page;
    private List<ProductData> products;
 
    private BigDecimal calculatedTotal;
 
    // Stores Total before scrolling
    private String displayedTotalBeforeScroll;
 
 
    // =========================================================
    // TAT-68 - Validate Total Sum
    // =========================================================
 
    @When("I open Part 3")
    public void iOpenPart3() {
 
        part3Page = new Part3Page(DriverManager.getDriver());
 
        part3Page.openPart3();
 
        // Capture Total before scrolling
        displayedTotalBeforeScroll = part3Page.getDisplayedTotal();
 
        System.out.println();
        System.out.println("=================================");
        System.out.println("TAT-68 - Part 3 opened");
        System.out.println("Displayed Total = "
                + displayedTotalBeforeScroll);
        System.out.println("=================================");
    }
 
 
    @And("I scroll through the product list")
    public void iScrollThroughTheProductList() {
 
        products = part3Page.getAllProducts();
 
        calculatedTotal = BigDecimal.ZERO;
 
        System.out.println();
        System.out.println("=================================");
        System.out.println("TAT-68 - Calculating Total");
        System.out.println("=================================");
 
        for (ProductData product : products) {
 
            String priceText = product.getPrice();
 
            String cleanPrice = priceText
                    .replace("Price:", "")
                    .replace("$", "")
                    .trim();
 
            BigDecimal price = new BigDecimal(cleanPrice);
 
            calculatedTotal = calculatedTotal.add(price);
 
            System.out.println(
                    "Brand: " + product.getBrand()
                    + " | Price: " + price + "$"
            );
        }
 
        System.out.println("---------------------------------");
        System.out.println(
                "Calculated Total = "
                + calculatedTotal
                + "$"
        );
        System.out.println("---------------------------------");
    }
 
 
    @Then("the total should match the sum of all item prices")
    public void theTotalShouldMatchTheSumOfAllItemPrices() {
 
        String cleanDisplayedTotal =
                displayedTotalBeforeScroll
                        .replace("Total:", "")
                        .replace("$", "")
                        .trim();
 
        BigDecimal displayedTotal =
                new BigDecimal(cleanDisplayedTotal);
 
        System.out.println();
        System.out.println("=================================");
        System.out.println("TAT-68 - Total Validation");
        System.out.println("=================================");
 
        System.out.println(
                "Calculated Total = "
                + calculatedTotal
                + "$"
        );
 
        System.out.println(
                "Displayed Total = "
                + displayedTotal
                + "$"
        );
 
        assertEquals(
                displayedTotal,
                calculatedTotal,
                "TAT-68 FAILED: Displayed Total does not match the sum of all item prices."
        );
 
        System.out.println();
        System.out.println("=================================");
        System.out.println("TAT-68 PASSED");
        System.out.println("Displayed Total matches all item prices.");
        System.out.println("=================================");
    }
 
 
    // =========================================================
    // TAT-70 - Validate FAKEIMAGE Names
    // =========================================================
 
    @When("I open Part 3 for image validation")
    public void iOpenPart3ForImageValidation() {
 
        part3Page = new Part3Page(DriverManager.getDriver());
 
        part3Page.openPart3();
 
        System.out.println("Part 3 opened for TAT-70.");
    }
 
 
    @And("I scroll through the product list and save all item image names")
    public void iScrollThroughTheProductListAndSaveAllItemImageNames() {
 
        products = part3Page.getAllProducts();
 
        System.out.println();
        System.out.println("=================================");
        System.out.println("TAT-70 - FAKEIMAGE Names");
        System.out.println("=================================");
 
        for (ProductData product : products) {
 
            System.out.println(
                    "Brand: "
                    + product.getBrand()
                    + " | Image: "
                    + product.getFakeImage()
            );
        }
        System.out.println(
                "Total products collected: "
                + products.size()
        );
    }
 
 
    @Then("all item image names should match the allowed image names")
    public void allItemImageNamesShouldMatchTheAllowedImageNames() {
 
        for (ProductData product : products) {
 
            String imageName = product.getFakeImage();
 
            System.out.println(
                    "Image: " + imageName
            );
 
            assertTrue(
                    imageName != null
                    && imageName.startsWith("FAKEIMAGE-")
                    && imageName.length()
                    > "FAKEIMAGE-".length(),
 
                    "TAT-70 FAILED: Invalid FAKEIMAGE name: "
                    + imageName
            );
        }
 
        System.out.println();
        System.out.println("=================================");
        System.out.println(
                "TAT-70 PASSED: All item image names are valid Android FAKEIMAGE names."
        );
        System.out.println("=================================");
    }
}
 
     
 