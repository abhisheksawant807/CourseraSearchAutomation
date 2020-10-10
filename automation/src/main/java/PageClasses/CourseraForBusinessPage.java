package PageClasses;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;

import BaseClasses.PageBaseClass;

public class CourseraForBusinessPage extends PageBaseClass {

	public CourseraForBusinessPage(WebDriver driver, ExtentTest logger) {
		super(driver, logger);
	}

	@FindBy(xpath = "//a[text()='Product']")
	public WebElement productDropDownButton;

	@FindBy(xpath = "//*[@id='menu-item-2442']/ul/li/a")
	public List<WebElement> productList;

	public void openCourseraForCampus() {
		try {
			String businessPage = driver.getWindowHandle();
			String formPage = null;

			waitForElement(productDropDownButton);
			Actions hover = new Actions(driver);
			hover.moveToElement(productDropDownButton).build().perform();
			
			selectElementInList(productList, prop.getProperty("productListItem"));

			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(businessPage)) {
					formPage = handle;
					break;
				}
			}
			closeWindow();
			switchToWindow(formPage);

			verifyPageTitle(prop.getProperty("formPageTitle"));
			reportPass("Successfully opened 'Coursera For Campus' page!");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

}
