package com.appium.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class Part3Page {

	private AndroidDriver driver;
	private WebDriverWait wait;

	// ==============================
	// Part 3 Locators
	// ==============================

	private final By part3Button = AppiumBy.accessibilityId("Part 3");

	private final By selectedTotal = AppiumBy.accessibilityId("Selected Total");

	private final By total = AppiumBy.accessibilityId("Total");

	private final By productList = AppiumBy.accessibilityId("Product List");

	private final By fakeBrand = AppiumBy.accessibilityId("fakeBrand");

	private final By fakePrice = AppiumBy.accessibilityId("fakePrice");

	private final By fakeImage = AppiumBy.accessibilityId("fakeImage");

	// ==============================
	// Constructor
	// ==============================

	public Part3Page(AndroidDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// ==============================
	// Open Part 3
	// ==============================

	public void openPart3() {

		wait.until(ExpectedConditions.elementToBeClickable(part3Button)).click();

		waitForPart3();

		System.out.println("Part 3 opened and positioned at top.");
	}

	// ==============================
	// Wait for Part 3
	// ==============================

	private void waitForPart3() {

		wait.until(ExpectedConditions.presenceOfElementLocated(productList));

		wait.until(ExpectedConditions.presenceOfElementLocated(fakePrice));
	}

	// ==============================
	// Get Displayed Total
	// ==============================

	public String getDisplayedTotal() {

		return wait.until(ExpectedConditions.presenceOfElementLocated(total)).getText().trim();
	}

	// ==============================
	// Get Selected Total
	// ==============================

	public String getSelectedTotal() {

		return wait.until(ExpectedConditions.presenceOfElementLocated(selectedTotal)).getText().trim();
	}

	// ==============================
	// Get Brand Totals
	// ==============================

	public List<String> getDisplayedBrandTotals() {

		By brandTotalLocator = AppiumBy.xpath("//android.widget.TextView[@content-desc='Brand Total']");

		return driver.findElements(brandTotalLocator).stream().map(WebElement::getText).toList();
	}

	// ==============================
	// Get All Products
	// ==============================

	public List<ProductData> getAllProducts() {

		List<ProductData> products = new ArrayList<>();

		final int MAX_SCROLLS = 80;

		int unchangedCount = 0;

		for (int scroll = 1; scroll <= MAX_SCROLLS; scroll++) {

			int productsBefore = products.size();

			/*
			 * Collect currently visible products.
			 */
			collectVisibleProducts(products);

			System.out.println("Scroll " + scroll + " -> Products collected: " + products.size());

			/*
			 * Check whether this scroll produced any new product.
			 */
			if (products.size() == productsBefore) {

				unchangedCount++;

			} else {

				unchangedCount = 0;
			}

			/*
			 * Do not immediately stop.
			 *
			 * A product can be partially visible at the bottom of the list.
			 *
			 * We allow several unchanged scrolls before performing the final collection.
			 */
			if (unchangedCount >= 6) {

				System.out.println();
				System.out.println("No new products found.");

				System.out.println("Performing final bottom collection...");

				/*
				 * One additional slow scroll.
				 */
				slowScrollUp();

				try {

					Thread.sleep(800);

				} catch (InterruptedException e) {

					Thread.currentThread().interrupt();
				}

				/*
				 * FINAL COLLECTION.
				 *
				 * This is important because the last product may become fully visible here.
				 */
				collectVisibleProducts(products);

				System.out.println("Final bottom collection -> Products collected: " + products.size());

				System.out.println("Reached bottom of Product List.");

				break;
			}

			/*
			 * Normal smooth scroll.
			 */
			slowScrollUp();

			try {

				Thread.sleep(600);

			} catch (InterruptedException e) {

				Thread.currentThread().interrupt();
			}
		}

		// ==============================
		// Final Product Output
		// ==============================

		System.out.println();
		System.out.println("=================================");

		System.out.println("Total products found: " + products.size());

		System.out.println("=================================");

		for (ProductData product : products) {

			System.out.println("Brand: " + product.getBrand() + " | Price: " + product.getPrice() + " | Image: "
					+ product.getFakeImage());
		}

		return products;
	}

	// ==============================
	// Collect Visible Products
	// ==============================

	private void collectVisibleProducts(List<ProductData> products) {

		try {

			WebElement productListElement = driver.findElement(productList);

			/*
			 * Each direct android.view.View under Product List represents a product card.
			 */
			List<WebElement> cards = productListElement.findElements(By.xpath("./android.view.View"));

			for (WebElement card : cards) {

				try {

					/*
					 * Read all values from the SAME product card.
					 */
					WebElement brandElement = card.findElement(fakeBrand);

					WebElement priceElement = card.findElement(fakePrice);

					WebElement imageElement = card.findElement(fakeImage);

					String brand = brandElement.getText().trim();

					String price = priceElement.getText().trim();

					String image = imageElement.getText().trim();

					String productKey = brand + "|" + price + "|" + image;

					/*
					 * Avoid collecting the same product again.
					 */
					boolean alreadyExists = false;

					for (ProductData existing : products) {

						String existingKey = existing.getBrand() + "|" + existing.getPrice() + "|"
								+ existing.getFakeImage();

						if (existingKey.equals(productKey)) {

							alreadyExists = true;

							break;
						}
					}

					if (!alreadyExists) {

						ProductData product = new ProductData(brand, price, image);

						products.add(product);

						System.out.println("New Product -> " + brand + " | " + price + " | " + image);
					}

				} catch (Exception e) {

					/*
					 * Ignore cards that are not currently completely rendered.
					 */
				}
			}

		} catch (Exception e) {

			System.out.println("Unable to collect visible products: " + e.getMessage());
		}
	}

	// Smooth Scroll
	// ==============================

	private void slowScrollUp() {

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

		Sequence swipe = new Sequence(finger, 1);

		swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 600, 1900));

		swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

		swipe.addAction(finger.createPointerMove(Duration.ofMillis(1200), PointerInput.Origin.viewport(), 600, 1100));

		swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Arrays.asList(swipe));
	}

	// ==============================
	// Product Data
	// ==============================

	public static class ProductData {

		private String brand;
		private String price;
		private String fakeImage;

		public ProductData(String brand, String price, String fakeImage) {

			this.brand = brand;
			this.price = price;
			this.fakeImage = fakeImage;
		}

		public String getBrand() {
			return brand;
		}

		public String getPrice() {
			return price;
		}

		public String getFakeImage() {
			return fakeImage;
		}

		@Override
		public String toString() {

			return "ProductData{" + "brand='" + brand + '\'' + ", price='" + price + '\'' + ", fakeImage='" + fakeImage
					+ '\'' + '}';
		}
	}
}