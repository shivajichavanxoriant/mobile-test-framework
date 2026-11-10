 
package com.appium.stepdefinitions;
 
import com.appium.driver.DriverManager;
import com.appium.pages.Part4Page;
 
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class Part4Steps {
 
    private static final Logger logger =
            LoggerFactory.getLogger(Part4Steps.class);
 
    private Part4Page part4Page;
 
    @When("I open Part 4")
    public void iOpenPart4() {
 
        part4Page =
                new Part4Page(DriverManager.getDriver());
 
        part4Page.openPart4();
 
        logger.info("Part 4 opened successfully.");
    }
 
    @And("I enable section {int} checkboxes")
    public void iEnableSectionCheckboxes(Integer section) {
 
    	switch (section) {
    	 
        case 1:
            part4Page.enableAllSection1Checkboxes();
            break;
     
        case 2:
            part4Page.enableAllSection1Checkboxes();
            part4Page.enableSection2SiblingCheckboxes();
            break;
     
        case 3:
        	 // Complete Section 1
            part4Page.enableAllSection1Checkboxes();
         
            // Complete Section 2 dynamically
            part4Page.enableSection2SiblingCheckboxes();
         
            // Complete Section 3 using Yes labels
            part4Page.enableSection3YesCheckboxes();
            break;
     
        default:
            throw new IllegalArgumentException(
                "Invalid Part 4 section: " + section);
    }
     
        logger.info(
                "Section {} checkboxes processed.",
                section);
    }
 
    @And("I click Submit")
    public void iClickSubmit() {
 
        part4Page.clickSubmit();
 
        logger.info("Submit button clicked.");
    }
 
    @Then("I should see Success popup for section {string}")
    public void iShouldSeeSuccessPopupForSection(String section) {
 
        String expectedMessage =
                "Step " + section + ": Success";
 
        part4Page.validateSuccess(expectedMessage);
 
        logger.info(
                "Validated success message: {}",
                expectedMessage);
    }
}
 