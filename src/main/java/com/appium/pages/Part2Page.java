package com.appium.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class Part2Page {

	private final AndroidDriver driver;
	private final WebDriverWait wait;

	// Part 2 button
	private final By part2Button = AppiumBy.accessibilityId("Part 2");

	/*
	 * Only actual brand rows.
	 *
	 * Category headers such as: ---A, ---B, ---I, ---J, ---K, ---L are excluded.
	 */
	private final By brandItems = AppiumBy.xpath("//android.widget.TextView" + "[@content-desc='brand' and "
			+ "not(starts-with(normalize-space(@text),'---'))]");

	public Part2Page(AndroidDriver driver) {

		this.driver = driver;

		/*
		 * Explicit wait. No Thread.sleep() is used.
		 */
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	/**
	 * Opens Part 2.
	 */
	public void openPart2() {

		wait.until(ExpectedConditions.elementToBeClickable(part2Button)).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(brandItems));

		System.out.println("Part 2 opened and positioned at top.");
	}

	/**
	 * Collects all brands from the complete list.
	 *
	 * IMPORTANT: The list is NOT sorted here. The original application order is
	 * preserved for TAT-64 alphabetical validation.
	 */
	public List<String> getAllBrands() {

		/*
		 * LinkedHashSet removes duplicates while preserving discovery order.
		 */
		Set<String> uniqueBrands = new LinkedHashSet<>();

		final int MAX_SCROLLS = 70;

		int noNewBrandCount = 0;

		String previousSignature = "";

		for (int scroll = 1; scroll <= MAX_SCROLLS; scroll++) {

			/*
			 * Get currently visible brands.
			 */
			List<WebElement> visibleBrands = driver.findElements(brandItems);

			String currentSignature = buildSignature(visibleBrands);

			int newBrandsThisScroll = 0;

			/*
			 * Collect every visible brand.
			 */
			for (WebElement element : visibleBrands) {

				try {

					String brand = element.getText();

					if (brand == null) {
						continue;
					}

					brand = brand.trim();

					if (brand.isEmpty()) {
						continue;
					}

					if (uniqueBrands.add(brand)) {
						newBrandsThisScroll++;
					}

				} catch (Exception e) {

					/*
					 * RecyclerView may recycle an element while it is being read.
					 */
				}
			}

			System.out.println("Scroll " + scroll + " -> Brands collected: " + uniqueBrands.size());

			/*
			 * Detect whether the visible list changed.
			 */
			if (currentSignature.equals(previousSignature) && newBrandsThisScroll == 0) {

				noNewBrandCount++;

			} else {

				noNewBrandCount = 0;
			}

			/*
			 * Stop only after 4 consecutive unchanged screens.
			 *
			 * This is intentionally conservative so brands such as Maserati are not missed.
			 */
			if (noNewBrandCount >= 4) {

				System.out.println("No new brands found after several scrolls.");

				System.out.println("Reached bottom of brand list.");

				break;
			}

			previousSignature = currentSignature;

			/*
			 * Slow controlled swipe.
			 *
			 * The distance is moderate, while the finger movement takes 1200 ms.
			 *
			 * This gives RecyclerView enough time to render intermediate brands.
			 */
			slowScrollUp();

			/*
			 * Wait for visible content to change.
			 */
			waitForListToChange(currentSignature);
		}

		/*
		 * Convert Set to List.
		 *
		 * Do NOT sort.
		 */
		List<String> allBrands = new ArrayList<>(uniqueBrands);

		/*
		 * Print final result.
		 */
		printFinalBrands(allBrands);

		/*
		 * Print category information.
		 */
		printCategory(allBrands, 'I');
		printCategory(allBrands, 'J');
		printCategory(allBrands, 'K');
		printCategory(allBrands, 'L');

		return allBrands;
	}

	/**
	 * Performs a slower controlled upward swipe.
	 */
	private void slowScrollUp() {

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

		Sequence swipe = new Sequence(finger, 1);

		/*
		 * Start near the lower-middle part of the application.
		 */
		swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 600, 1900));

		/*
		 * Finger touches screen.
		 */
		swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

		/*
		 * Move upward slowly.
		 *
		 * 1200 ms makes the swipe slower than mobile: swipeGesture.
		 */
		swipe.addAction(finger.createPointerMove(Duration.ofMillis(1200), PointerInput.Origin.viewport(), 600, 1100));

		/*
		 * Release finger.
		 */
		swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Arrays.asList(swipe));
	}

	/**
	 * Waits until visible brand content changes.
	 *
	 * No Thread.sleep().
	 */
	private void waitForListToChange(String previousSignature) {

		try {

			wait.until(driver -> {

				String currentSignature = buildSignature(driver.findElements(brandItems));

				return !currentSignature.equals(previousSignature);
			});

		} catch (Exception e) {

			/*
			 * If content does not change within the explicit wait, the next iteration will
			 * check the list again.
			 */
		}
	}

	/**
	 * Creates a signature from visible brands.
	 */
	private String buildSignature(List<WebElement> elements) {

		StringBuilder signature = new StringBuilder();

		for (WebElement element : elements) {

			try {

				String text = element.getText();

				if (text != null && !text.trim().isEmpty()) {

					signature.append(text.trim()).append("|");
				}

			} catch (Exception e) {

				/*
				 * Ignore recycled RecyclerView elements.
				 */
			}
		}

		return signature.toString();
	}

	/**
	 * Prints all collected brands.
	 */
	private void printFinalBrands(List<String> brands) {

		System.out.println();

		System.out.println("=================================");

		System.out.println("Total brands found: " + brands.size());

		System.out.println("=================================");

		for (String brand : brands) {

			System.out.println(brand);
		}
	}

	/**
	 * Prints brands belonging to a category.
	 */
	private void printCategory(List<String> brands, char letter) {

		int count = 0;

		System.out.println();

		System.out.println("Category " + letter + ":");

		for (String brand : brands) {

			if (brand != null && !brand.isEmpty() && Character.toUpperCase(brand.charAt(0)) == letter) {

				System.out.println(" " + brand);

				count++;
			}
		}

		System.out.println("Category " + letter + " count = " + count);
	}
}
