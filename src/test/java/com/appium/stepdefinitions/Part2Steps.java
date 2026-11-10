 
package com.appium.stepdefinitions;
 
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
 
import com.appium.driver.DriverManager;
import com.appium.pages.Part2Page;
 
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
public class Part2Steps {
 
    private static final Logger logger =
            LoggerFactory.getLogger(Part2Steps.class);
 
    private Part2Page part2Page;
    private List<String> brands;
 
    @When("I open Part 2")
    public void iOpenPart2() {
 
        part2Page =
                new Part2Page(DriverManager.getDriver());
 
        part2Page.openPart2();
    }
 
    @When("I scroll through the brand list")
    public void iScrollThroughTheBrandList() {
 
        /*
         * Collect all brands from Part 2.
         */
        brands = part2Page.getAllBrands();
 
        logger.info("Total brands found: {}", brands.size());
    }
 
    // =====================================================
    // TAT-64
    // =====================================================
 
    @Then("the brand list should be in alphabetical order")
    public void theBrandListShouldBeInAlphabeticalOrder() {
 
        /*
         * First verify that all expected brands
         * have been collected.
         */
        Assert.assertEquals(
                brands.size(),
                64,
                "TAT-64 FAILED: Expected 64 brands but found "
                        + brands.size()
        );
 
        /*
         * Verify alphabetical order.
         */
        for (int i = 0; i < brands.size() - 1; i++) {
 
            String current = brands.get(i);
            String next = brands.get(i + 1);
 
            Assert.assertTrue(
                    current.compareToIgnoreCase(next) <= 0,
                    "TAT-64 FAILED: Brand list is NOT in "
                            + "alphabetical order. "
                            + current
                            + " comes before "
                            + next
            );
        }
 
        logger.info(
                "TAT-64 PASSED: 64 brands are in alphabetical order."
        );
    }
 
    // =====================================================
    // TAT-66
    // =====================================================
 
    @Then("the following categories should be empty")
    public void theFollowingCategoriesShouldBeEmpty(
            DataTable dataTable) {
 
        List<String> emptyCategories =
                dataTable.asList();
 
        for (String category : emptyCategories) {
 
            boolean found = false;
 
            for (String brand : brands) {
 
                if (brand != null
                        && !brand.isEmpty()
                        && brand.substring(0, 1)
                                .equalsIgnoreCase(category)) {
 
                    found = true;
                    break;
                }
            }
 
            Assert.assertFalse(
                    found,
                    "TAT-66 FAILED: Category "
                            + category
                            + " contains a brand."
            );
 
            logger.info(
                    "TAT-66: Category {} is empty.",
                    category
            );
        }
 
        logger.info(
                "TAT-66 PASSED: All expected empty categories are empty."
        );
    }
 
    // =====================================================
    // TAT-67
    // =====================================================
 
    @Then("the following categories should have at least 3 items")
    public void theFollowingCategoriesShouldHaveAtLeast3Items(
            DataTable dataTable) {
 
        List<String> categories =
                dataTable.asList();
 
        for (String category : categories) {
 
            int count = 0;
 
            for (String brand : brands) {
 
                if (brand != null
                        && !brand.isEmpty()
                        && brand.substring(0, 1)
                                .equalsIgnoreCase(category)) {
 
                    count++;
                }
            }
 
            Assert.assertTrue(
                    count >= 3,
                    "TAT-67 FAILED: Category "
                            + category
                            + " has only "
                            + count
                            + " item(s). "
                            + "Expected at least 3."
            );
 
            logger.info(
                    "TAT-67: Category {} has {} items.",
                    category,
                    count
            );
        }
 
        logger.info(
                "TAT-67 PASSED: All expected categories have at least 3 items."
        );
    }
}
 