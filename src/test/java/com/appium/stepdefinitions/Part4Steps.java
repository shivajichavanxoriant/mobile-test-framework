package com.appium.stepdefinitions;

import static org.testng.Assert.assertTrue;

import com.appium.driver.DriverManager;
import com.appium.pages.Part4Page;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Part4Steps {

	private Part4Page part4Page;

	// =====================================================
	// TAT-71
	// Open Part 4
	// =====================================================
	@When("I open Part 4")
	public void iOpenPart4() {
		part4Page = new Part4Page(DriverManager.getDriver());
		part4Page.openPart4();
		System.out.println("Part 4 opened.");
	}

	// =====================================================
	// TAT-71
	// Enable Section 1 checkboxes
	// =====================================================
	@And("I enable section 1 checkboxes")
	public void iEnableSection1Checkboxes() {
		part4Page.enableSection1Checkboxes();
		System.out.println("Section 1 checkboxes enabled.");
	}

	// =====================================================
	// TAT-72
	// Enable Section 2 checkboxes
	// =====================================================
	@And("I enable section 2 checkboxes")
	public void iEnableSection2Checkboxes() {
		part4Page.enableSection2Checkboxes();
		System.out.println("Section 2 checkboxes enabled.");
	}

	// =====================================================
	// TAT-73
	// Enable Section 3 checkboxes
	// =====================================================
	@And("I enable section 3 checkboxes")
	public void iEnableSection3Checkboxes() {
		part4Page.enableSection3Checkboxes();
		System.out.println("Section 3 checkboxes enabled.");
	}

	// =====================================================
	// Common Step
	// Click Submit
	// =====================================================
	@And("I click Submit")
	public void iClickSubmit() {
		part4Page.clickSubmit();
		System.out.println("Submit clicked.");
	}

	// =====================================================
	// Common Step
	// Validate Success popup
	// =====================================================
	@Then("I should see Success popup for section {string}")
	public void iShouldSeeSuccessPopup(String section) {
		boolean success = part4Page.validateSuccess(section);
		assertTrue(success, "Success popup not displayed for section " + section);
		System.out.println("Success popup validated for section " + section + ".");
	}
}
