package Assignment.TMobile;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

public class Filter {

	WebDriver driver;
	WebDriverWait wait;
	String phone_device_link = "//a[text()='Phones & devices']";
	String tabletLink = "//a[text()='Tablets']";
	String pricingBlock = "//tmo-pricing-starts-at";

	String tabletHeading = "//h1[contains(text(),'Tablets')]";
	String filter_heading = "//h2[contains(text(),'Filters')]";
	String left_panel_Deal_filter_Category = "//legend[contains(text(),'Deals')]";
	String left_panel_filter_option_start = "//legend[contains(text(),'";
	String common_end = "')]";
	String option_start = "//div[@aria-label='";
	String option_middle = "']//span[contains(text(),'";
	String filter_added_indication_start = "//div[@data-testid='filter-chip' and contains(text(),'";
	String option_common_checkbox_end = "']//span[starts-with(@class,'mat-checkbox-inner-container')]";
	String option_common_start = "//div[@aria-label='";
	String option_common_middle = "']//mat-checkbox[";
	String option_common_end = "]//span[starts-with(@class,'mat-checkbox-inner-container')]";
	String option_common_product_name_end = "]//span[starts-with(@class,'mat-checkbox-inner-container')]//following-sibling::span[contains(@class,'label')]//span[2]";

	@BeforeTest
	public void setUp() {
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get("https://www.t-mobile.com/");
		mouseOverAndClick(getTheElement(phone_device_link, "xpath"), getTheElement(tabletLink, "xpath"));

		initialiseExplicitWait(driver, 200)
				.until(ExpectedConditions.presenceOfElementLocated(getByLocator(pricingBlock, "xpath")));

		Assert.assertTrue(initialiseExplicitWait(driver, 30)
				.until(ExpectedConditions.presenceOfElementLocated(getByLocator(tabletHeading, "xpath")))
				.isDisplayed());

		scrollIntoView(driver, getTheElement(filter_heading, "xpath"));
		initialiseExplicitWait(driver, 200)
				.until(ExpectedConditions.elementToBeClickable(getByLocator(left_panel_Deal_filter_Category, "xpath")));

	}

	@Test
	public void test_Filter() {
		selectFilterOption("Deals", "All");
		selectFilterOption("Brands", "Alcatel", "Samsung", "TCL");
		selectFilterOption("Operating System", "All");
	}

	public void selectFilterOption(String... strings) {
		int count = 1;
		String filterCategory = null;
		for (String str : strings) {

			if (count == 1) {
				filterCategory = str;
				click(getTheElement(left_panel_filter_option_start + filterCategory + common_end, "xpath"));
			} else {
				if (!str.equalsIgnoreCase("All")) {

					click(getTheElement(option_start + filterCategory + option_middle + str + common_end, "xpath"));
					Assert.assertTrue(initialiseExplicitWait(driver, 30)
							.until(ExpectedConditions.presenceOfElementLocated(
									getByLocator(filter_added_indication_start + str + common_end, "xpath")))
							.isDisplayed());

				} else {
					
					int totalOptioncount = getAllElements(
							option_common_start + filterCategory + option_common_checkbox_end, "xpath").size();
					
					for (int i = 1; i <= totalOptioncount; i++) {

						String productname = getTheElement(option_common_start + filterCategory + option_common_middle
								+ i + option_common_product_name_end, "xpath").getText();
						
						click(getTheElement(
								option_common_start + filterCategory + option_common_middle + i + option_common_end,
								"xpath"));
						
						Assert.assertTrue(initialiseExplicitWait(driver, 30)
								.until(ExpectedConditions.presenceOfElementLocated(getByLocator(
										filter_added_indication_start + productname + common_end, "xpath")))
								.isDisplayed());

					}

				}

			}
			count++;
		}

	}

	public void mouseOverAndClick(WebElement element1, WebElement element2) {
		Actions action = new Actions(driver);
		action.moveToElement(element1).moveToElement(element2).click().build().perform();
	}

	public WebElement getTheElement(String locator, String locator_type) {
		WebElement element = null;

		if (locator_type.equalsIgnoreCase("xpath")) {
			element = driver.findElement(By.xpath(locator));
		}
		if (locator_type.equalsIgnoreCase("css")) {
			element = driver.findElement(By.cssSelector(locator));
		}
		return element;
	}

	public WebDriverWait initialiseExplicitWait(WebDriver driver, int waitTime) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
		return wait;
	}

	public void click(WebElement element) {
		element.click();
	}

	public By getByLocator(String locatorText, String locatorType) {

		By by = null;

		if (locatorType.equalsIgnoreCase("xpath")) {
			by = By.xpath(locatorText);
		}
		if (locatorType.equalsIgnoreCase("css")) {
			by = By.cssSelector(locatorText);
		}
		return by;

	}

	public void scrollIntoView(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public List<WebElement> getAllElements(String commonLocator, String locatorType) {
		List<WebElement> elements = null;
		if (locatorType.equalsIgnoreCase("xpath")) {
			elements = driver.findElements(By.xpath(commonLocator));
		}
		if (locatorType.equalsIgnoreCase("css")) {
			elements = driver.findElements(By.cssSelector(commonLocator));
		}
		return elements;
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
