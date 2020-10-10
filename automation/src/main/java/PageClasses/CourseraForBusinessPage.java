package PageClasses;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;

import BaseClasses.PageBaseClass;

public class CourseraForBusinessPage extends PageBaseClass {

	// Initialising the driver and logger by passing them to the parent constructor
	public CourseraForBusinessPage(WebDriver driver, ExtentTest logger) {
		super(driver, logger);
	}

	// These @FindBy annotations are used to locate specific WebElements of specific webpages
	@FindBy(xpath = "//a[text()='Product']")
	public WebElement productDropDownButton;

	@FindBy(xpath = "//*[@id='menu-item-2442']/ul/li/a")
	public List<WebElement> productList;

	public void openCourseraForCampus() {
		try {
			String businessPage = driver.getWindowHandle(); // Initialising the current page window handle
			String formPage = null;

			waitForElement(productDropDownButton); // Waiting for the specific element
			// Hovering over productDropDownButton
			Actions hover = new Actions(driver);
			hover.moveToElement(productDropDownButton).build().perform();
			
			// Selecting the specified element
			selectElementInList(productList, prop.getProperty("productListItem"));

			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(businessPage)) {
					// Initialising the formPage window handle
					formPage = handle;
					break;
				}
			}
			closeWindow(); // Closing the current window
			switchToWindow(formPage); // Switching to formPage window handle

			// Verifying the formPage title
			verifyPageTitle(prop.getProperty("formPageTitle"));
			// Logging the successful verification of page title
			reportPass("Successfully opened 'Coursera For Campus' page!");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

}
