package com.appium.stepdefinitions;

import static org.testng.Assert.assertTrue;

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

	// =====================================================
	// TAT-70
	// Open Part 3
	// =====================================================

	@When("I open Part 3 for image validation")
	public void iOpenPart3ForImageValidation() {

		part3Page = new Part3Page(DriverManager.getDriver());

		part3Page.openPart3();

		System.out.println("Part 3 opened for TAT-70.");
	}

	// =====================================================
	// TAT-70
	// Scroll through Product List
	// =====================================================

	@And("I scroll through the product list and save all item image names")
	public void iScrollThroughTheProductListAndSaveAllItemImageNames() {

		products = part3Page.getAllProducts();

		System.out.println();
		System.out.println("=================================");
		System.out.println("TAT-70 - FAKEIMAGE Names");
		System.out.println("=================================");

		for (ProductData product : products) {

			System.out.println("Brand: " + product.getBrand() + " | Image: " + product.getFakeImage());
		}

		System.out.println("Total products collected: " + products.size());
	}

	// =====================================================
	// TAT-70 - Validate FAKEIMAGE names for Android
	// =====================================================

	@Then("all item image names should match the allowed image names")
	public void allItemImageNamesShouldMatchTheAllowedImageNames() {

		for (ProductData product : products) {

			String imageName = product.getFakeImage();

			System.out.println("Image: " + imageName);

			// Validate Android FAKEIMAGE name format
			assertTrue(
					imageName != null && imageName.startsWith("FAKEIMAGE-")
							&& imageName.length() > "FAKEIMAGE-".length(),
					"TAT-70 FAILED: Invalid FAKEIMAGE name: " + imageName);
		}

		System.out.println();
		System.out.println("=================================");
		System.out.println("TAT-70 PASSED: All item image names " + "are valid Android FAKEIMAGE names.");
		System.out.println("=================================");
	}
}
