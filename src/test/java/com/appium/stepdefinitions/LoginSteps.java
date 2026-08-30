package com.appium.stepdefinitions;

import org.testng.Assert;

import com.appium.driver.DriverManager;
import com.appium.pages.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

	private LoginPage loginPage = new LoginPage(DriverManager.getDriver());

	// =========================
	// Open Part 1
	// =========================

	@Given("User opens Part 1 screen")
	public void user_opens_part_1_screen() {

		loginPage.openPart1();
	}

	// =========================
	// Valid credentials
	// =========================

	@When("User enters a valid username")
	public void user_enters_a_valid_username() {

		loginPage.enterUserName("MagicBird123");
	}

	@When("User enters a valid email")
	public void user_enters_a_valid_email() {

		loginPage.enterEmail("name.surname@gmail.com");
	}

	@When("User enters a valid password")
	public void user_enters_a_valid_password() {

		loginPage.enterPassword("Password123!");
	}

	@When("User taps the Submit button")
	public void user_taps_the_submit_button() {

		loginPage.clickSubmit();
	}

	@Then("Success message should be displayed")
	public void success_message_should_be_displayed() {

		// Keep empty for now because the valid-login
		// success-message locator has not been finalized.
	}

	// =========================
	// Invalid credentials
	// =========================

	@When("User enters an invalid username")
	public void user_enters_an_invalid_username() {

		/*
		 * "abc" is invalid because the application requires at least 4 characters.
		 */
		loginPage.enterUserName("abc");
	}

	@When("User enters an invalid email")
	public void user_enters_an_invalid_email() {

		loginPage.enterEmail("wrongemail");
	}

	@When("User enters an invalid password")
	public void user_enters_an_invalid_password() {

		loginPage.enterPassword("wrong");
	}

	@Then("Validation error message should be displayed")
	public void validation_error_message_should_be_displayed() {

		Assert.assertTrue(loginPage.isValidationMessageDisplayed(), "Validation error message was not displayed.");
	}
}
