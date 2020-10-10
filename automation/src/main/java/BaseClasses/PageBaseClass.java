package BaseClasses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import PageClasses.HomePage;

public class PageBaseClass extends BaseTestClass {

	public PageBaseClass(WebDriver driver, ExtentTest logger) {
		this.driver = driver;
		this.logger = logger;
	}

	public static String homePageURL;
	public static String homePageTitle;
	public static String newHomePageTitle;

	/****************** OpenApplication ***********************/
	public HomePage openApplication() {
		try {
			// Assigning global properties from 'Config.properties' file
			homePageURL = prop.getProperty("homePageURL");
			homePageTitle = prop.getProperty("homePageTitle");
			newHomePageTitle = prop.getProperty("newHomePageTitle");
			
			reportInfo("Opening the COURSERA Website...");
			openURL(homePageURL); // Opening coursera.org
			disableInfobar(); // Disabling infobar in Opera

			// Verifying Home page title
			verifyPageTitle(homePageTitle, newHomePageTitle);
			reportPass("Successfully Opened COURSERA!");

			// Creating a new HomePage object, initialising the driver and logger, and returning the object
			HomePage homePage = new HomePage(driver, logger);
			PageFactory.initElements(driver, homePage);
			return homePage;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return null;
	}

	/****************** Enter Text ***********************/
	public void enterText(WebElement element, String elementName, String data) {
		try {
			// Waiting for the element to prevent 'NoSuchElementException' 
			// or 'StaleElementReferenceException'
			waitForElement(element); 
			if (data.equals("___")) {
				// Logging the blank status of an element
				reportInfo(elementName + " : This field has been deliberately kept blank!");
				return;
			}
			element.sendKeys(data);  // Entering text in the element
			
			// Logging the successful text entry in the element
			reportPass(data + " - Entered successfully in Element : " + elementName);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Click Element ***********************/
	public void elementClick(WebElement element, String elementName) {
		try {
			waitForPageLoad();
			waitForElement(element);
			element.click(); // Clicking the element
			
			// Logging the successful click of the element
			reportPass(elementName + " : Element Clicked Successfully!");
			waitForPageLoad();
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Select Item in List Drop Down ******************/
	public void selectElementInList(List<WebElement> elementList, String Value) {
		try {
			waitForElements(elementList); // Waiting for the WebElement List
			for (WebElement listItem : elementList) {
				if (listItem.getText().trim().equalsIgnoreCase(Value)) {
					// Selecting the matched listItem
					elementClick(listItem, "Suggestion Box option"); 
					break;
				}
			}
			//Logging the successful selection of the value
			reportPass("Selected the Defined Value : " + Value);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Select value From DropDown ***********************/
	public void selectDropDownValue(WebElement element, String elementName, String value) {
		try {
			waitForElement(element);
			Select select = new Select(element);
			if (value.equals("___")) {
				reportInfo(elementName + " : This field has been deliberately kept blank!");
				return;
			}
			select.selectByVisibleText(value); // Selecting the specific option
			
			// Logging the successful selection of a specific option
			reportPass("Selected the value : " + value + " from the select element : " + elementName);
			waitForPageLoad();
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	/****************** Verify Page Title ***********************/
	public void verifyPageTitle(String expectedTitle) {
		try {
			// Intentional wait for to prevent 'AssertionError' in Firefox
			firefoxWait(2); 
			waitForPageLoad();
			String actualTitle = driver.getTitle();
			
			// Checking the actual title with the expected title
			Assert.assertEquals(actualTitle, expectedTitle);
			// Logging the successful match of actual and expected titles
			reportPass("Actual Title : " + actualTitle + " - equals to Expected Title : " + expectedTitle);
			waitForPageLoad();
		} catch (AssertionError e) {
			reportFail(e.getMessage());
		}
	}
	
	// Overloaded method for verifying HomePageTitle (due to dynamic title of coursera homepage)
	public void verifyPageTitle(String expectedTitle1, String expectedTitle2) {
		try {
			firefoxWait(2);
			waitForPageLoad();
			String actualTitle = driver.getTitle();
			if(actualTitle.equals(expectedTitle1)) {
				Assert.assertEquals(actualTitle, expectedTitle1);
				reportPass("Actual Title : " + actualTitle + " - equals to Expected Title : " + expectedTitle1);
			}
			else {
				Assert.assertEquals(actualTitle, expectedTitle2);
				reportPass("Actual Title : " + actualTitle + " - equals to Expected Title : " + expectedTitle2);
			}
			waitForPageLoad();
		} catch (AssertionError e) {
			reportFail(e.getMessage());
		}
	}


	/****************** Disable infobar in Opera Driver ***************************/
	public void disableInfobar() {
		if (driver instanceof OperaDriver) {
			try {
				waitForPageLoad();
				WebElement pageBody = getPageBody();
				pageBody.sendKeys(Keys.TAB); // Press 'TAB' key
				pageBody.sendKeys(Keys.SHIFT, Keys.TAB); // Press 'SHIFT+TAB' keys
				waitFor(1);

				// Press 'ENTER' key
				Robot r = new Robot();
				r.keyPress(KeyEvent.VK_ENTER);
				r.keyRelease(KeyEvent.VK_ENTER);
				waitForPageLoad();

			} catch (AWTException e) {
				reportFail(e.getMessage());
			}
		}
	}
}
