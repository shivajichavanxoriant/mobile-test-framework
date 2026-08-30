package com.appium.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class Part3Page {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	// =====================================================
	// Part 3 locators
	// =====================================================

	private final By part3Button = AppiumBy.accessibilityId("Part 3");

	private final By selectedTotal = AppiumBy.accessibilityId("Selected Total");

	private final By total = AppiumBy.accessibilityId("Total");

	private final By productList = AppiumBy.accessibilityId("Product List");

	// Product card elements
	private final By fakeBrand = AppiumBy.accessibilityId("fakeBrand");

	private final By fakePrice = AppiumBy.accessibilityId("fakePrice");

	private final By fakeImage = AppiumBy.accessibilityId("fakeImage");

	// =====================================================
	// Constructor
	// =====================================================

	public Part3Page(AndroidDriver driver) {

		this.driver = driver;

		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// =====================================================
	// Open / wait for Part 3
	// =====================================================

	public void openPart3() {

		wait.until(ExpectedConditions.elementToBeClickable(part3Button)).click();

		waitForPart3();
	}

	// =====================================================
	// Wait for Part 3 product list
	// =====================================================

	public void waitForPart3() {

		wait.until(ExpectedConditions.presenceOfElementLocated(productList));

		wait.until(ExpectedConditions.presenceOfElementLocated(fakePrice));

		System.out.println("Part 3 opened and positioned at top.");
	}

	// =====================================================
	// Get displayed Total
	// =====================================================

	public String getDisplayedTotal() {

		return wait.until(ExpectedConditions.presenceOfElementLocated(total)).getText().trim();
	}

	// =====================================================
	// Get Selected Total
	// =====================================================

	public String getSelectedTotal() {

		return wait.until(ExpectedConditions.presenceOfElementLocated(selectedTotal)).getText().trim();
	}

	// =====================================================
	// TAT-69
	// Get displayed Brand Totals
	// =====================================================

	public List<String> getDisplayedBrandTotals() {
		 
	    List<WebElement> elements =
	            driver.findElements(
	                    AppiumBy.xpath(
	                            "//android.widget.TextView[@content-desc='Brand Total']"
	                    )
	            );
	 
	    List<String> totals = new ArrayList<>();
	 
	    for (WebElement element : elements) {
	 
	        try {
	            String text = element.getText().trim();
	 
	            if (!text.isEmpty()) {
	                totals.add(text);
	            }
	 
	        } catch (Exception e) {
	            // Ignore recycled elements
	        }
	    }
	 
	    System.out.println();
	    System.out.println("=================================");
	    System.out.println("Displayed Brand Totals");
	    System.out.println("=================================");
	 
	    for (String total : totals) {
	        System.out.println(total);
	    }
	 
	    return totals;
	}
	 
	// =====================================================
	// Collect all products
	// =====================================================

	public List<ProductData> getAllProducts() {

		/*
		 * LinkedHashMap preserves discovery order.
		 *
		 * Key: Brand + Price + FakeImage
		 *
		 * This prevents the same recycled card from being counted more than once.
		 */
		Map<String, ProductData> uniqueProducts = new LinkedHashMap<>();

		final int MAX_SCROLLS = 60;

		int noNewProductCount = 0;

		String previousSignature = "";

		for (int scroll = 1; scroll <= MAX_SCROLLS; scroll++) {

			// Get currently visible products
			List<WebElement> brands = driver.findElements(fakeBrand);

			List<WebElement> prices = driver.findElements(fakePrice);

			List<WebElement> images = driver.findElements(fakeImage);

			/*
			 * Only process indexes that exist in all three lists.
			 */
			int visibleCount = Math.min(brands.size(), Math.min(prices.size(), images.size()));

			int newProductsThisScroll = 0;

			for (int i = 0; i < visibleCount; i++) {

				try {

					String brand = brands.get(i).getText().trim();

					String price = prices.get(i).getText().trim();

					String image = images.get(i).getText().trim();

					if (brand.isEmpty() || price.isEmpty() || image.isEmpty()) {

						continue;
					}

					String key = brand + "|" + price + "|" + image;

					if (!uniqueProducts.containsKey(key)) {

						uniqueProducts.put(key, new ProductData(brand, price, image));

						newProductsThisScroll++;
					}

				} catch (Exception e) {

					/*
					 * RecyclerView can recycle an element while we are reading it.
					 */
				}
			}

			// Create signature of visible products
			String currentSignature = buildSignature(brands, prices, images);

			System.out.println("Scroll " + scroll + " -> Products collected: " + uniqueProducts.size());

			/*
			 * Detect whether the screen stopped producing new products.
			 */
			if (currentSignature.equals(previousSignature) && newProductsThisScroll == 0) {

				noNewProductCount++;

			} else {

				noNewProductCount = 0;
			}

			/*
			 * Stop after 4 consecutive unchanged screens.
			 *
			 * Same safety approach as Part 2.
			 */
			if (noNewProductCount >= 4) {

				System.out.println("No new products found.");

				System.out.println("Reached bottom of Product List.");

				break;
			}

			previousSignature = currentSignature;

			/*
			 * Small controlled scroll.
			 *
			 * 0.15 reduces the chance of skipping products.
			 */
			scrollUp();

			waitForListToChange(currentSignature);
		}

		List<ProductData> products = new ArrayList<>(uniqueProducts.values());

		System.out.println();

		System.out.println("=================================");

		System.out.println("Total products found: " + products.size());

		System.out.println("=================================");

		for (ProductData product : products) {

			System.out.println(product);
		}

		return products;
	}

	// =====================================================
	// Small controlled scroll
	// =====================================================

	private void scrollUp() {

		int width = driver.manage().window().getSize().getWidth();

		int height = driver.manage().window().getSize().getHeight();

		driver.executeScript("mobile: swipeGesture", Map.of("left", 100, "top", 500, "width", width - 200, "height",
				height - 700, "direction", "up", "percent", 0.15));
	}

	// =====================================================
	// Wait for list to change
	// =====================================================

	private void waitForListToChange(String previousSignature) {

		try {

			wait.until(driver -> {

				String currentSignature = buildSignature(driver.findElements(fakeBrand), driver.findElements(fakePrice),
						driver.findElements(fakeImage));

				return !currentSignature.equals(previousSignature);
			});

		} catch (Exception e) {

			/*
			 * Timeout is handled by the next loop.
			 *
			 * No Thread.sleep().
			 */
		}
	}
	// =====================================================
	// Build visible-product signature
	// =====================================================

	private String buildSignature(List<WebElement> brands, List<WebElement> prices, List<WebElement> images) {

		StringBuilder signature = new StringBuilder();

		int count = Math.min(brands.size(), Math.min(prices.size(), images.size()));

		for (int i = 0; i < count; i++) {

			try {

				signature.append(brands.get(i).getText()).append("|");

				signature.append(prices.get(i).getText()).append("|");

				signature.append(images.get(i).getText()).append("||");

			} catch (Exception e) {

				// Ignore recycled elements.
			}
		}

		return signature.toString();
	}

	// =====================================================
	// Product data
	// =====================================================

	public static class ProductData {

		private final String brand;
		private final String price;
		private final String fakeImage;

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

			return "Brand: " + brand + " | Price: " + price + " | Image: " + fakeImage;
		}
	}
}
