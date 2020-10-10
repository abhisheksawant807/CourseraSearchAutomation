package BaseClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Utilities.DateUtil;
import Utilities.ExtentReportManager;

public class BaseTestClass {

	public WebDriver driver;
	public ExtentTest logger;
	public static ExtentReports report;
	public static String userDir;
	public static String driverDir;
	public static Properties prop;

	
	/****************** Load the properties ***********************/
	@BeforeSuite(alwaysRun = true)
	public void loadProperties() {
		FileInputStream fis = null;
		try {
			userDir = System.getProperty("user.dir");
			prop = new Properties();
			fis = new FileInputStream(userDir + "/test-data/Config.properties");
			prop.load(fis);
			report = ExtentReportManager.getReportInstance();
			driverDir = userDir + prop.getProperty("driverRelPath");
			fis.close();
		} catch (Exception e) {
			reportFail(e.getMessage());
		} 
	}
	
	/****************** Invoke Browser ***********************/
	public void invokeBrowser(String browserName) {
		try {
			if (browserName.equalsIgnoreCase("Chrome")) {
				reportInfo("Opening CHROME browser...");
				System.setProperty("webdriver.chrome.driver", driverDir + "chromedriver.exe");

				ChromeOptions co = new ChromeOptions();
				co.setAcceptInsecureCerts(true); // accepts insecure website certifications
				co.addArguments("--disable-notifications"); // disables notifications
				co.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" }); // disables infobars
				co.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT); // accepts any unexpected alert
				driver = new ChromeDriver(co);
				reportPass("Successfully Loaded CHROME browser!");

			} else if (browserName.equalsIgnoreCase("Firefox")) {
				reportInfo("Opening FIREFOX browser...");
				System.setProperty("webdriver.gecko.driver", driverDir + "geckodriver.exe");

				FirefoxOptions fo = new FirefoxOptions();
				fo.addPreference("dom.webnotifications.enabled", false); // disables notifications
				fo.addArguments("--disable-notifications");
				fo.setAcceptInsecureCerts(true);
				fo.addArguments("--disable-infobars"); // disables infobars
				fo.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
				driver = new FirefoxDriver(fo);
				reportPass("Successfully Loaded FIREFOX browser!");

			} else if (browserName.equalsIgnoreCase("Opera")) {
				reportInfo("Opening OPERA browser...");
				System.setProperty("webdriver.opera.driver", driverDir + "operadriver.exe");

				OperaOptions options = new OperaOptions();
				options.addArguments("--disable-notifications");
				options.addArguments("--disable-infobars");
				options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
				driver = new OperaDriver(options);
				reportPass("Successfully Loaded OPERA browser!");
			} else {
				System.setProperty("webdriver.ie.driver", driverDir + "IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}

			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //Implicit timeout
			driver.manage().window().maximize(); // Maximises the browser window

		} catch (Exception e) {
			// Performing the necessary actions upon the detection of an exception or error
			reportFail(e.getMessage()); 
		}
	}

	
	@AfterTest(alwaysRun = true)
	public void quitBrowser() {
		driver.quit(); // Closes the current browser
	}

	@AfterSuite(alwaysRun = true)
	public void endReport() {
		report.flush(); // Flushes the html report
		
		// Assigning null values to these static variables after the test completion
		// to prevent data leakage
		userDir = null; 
		prop = null;
		driverDir = null;
	}
	

	/************** Basic driver methods *************/
	public void switchToWindow(String windowHandle) {
		try {
			driver.switchTo().window(windowHandle); // Switching to a different window
			firefoxWait(2); // Timeout for Firefox
			waitForPageLoad(); // Page Load Timeout
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	public void navigateTo(String url) {
		driver.navigate().to(url); // Navigating to a specific URL
		firefoxWait(1);
		waitForPageLoad();
	}

	public void openURL(String url) {
		driver.get(url); // Opening a specific webpage
		firefoxWait(1);
		waitForPageLoad();
	}

	public void refreshPage() {
		driver.navigate().refresh(); // Refreshing the page 
		firefoxWait(1);
		waitForPageLoad();
	}

	public void goBack() {
		driver.navigate().back(); // Navigating back to previous page
		firefoxWait(1);
		waitForPageLoad();
	}

	public void closeWindow() {
		driver.close(); // Closes the current window of current browser
	}

	/****************** Reporting Functions ***********************/
	public void reportFail(String reportString) {
		takeScreenShotOnFailure(); // Takes screenshot upon an exception
		logger.log(Status.FAIL, reportString); // Logs the exception or error in the html report
		Assert.fail(reportString); // Terminates test execution and displays the error in console
	}

	public void reportPass(String reportString) {
		logger.log(Status.PASS, reportString); // Logs a 'passed' action in the html report
	}

	public void reportInfo(String reportString) {
		logger.log(Status.INFO, reportString); // Logs specific info regarding an action in html report
	}

	/****************** Capture Screen Shot ***********************/
	public void takeScreenShotOnFailure() {

		try {
			TakesScreenshot takeScreenShot = (TakesScreenshot) driver;
			File sourceFile = takeScreenShot.getScreenshotAs(OutputType.FILE);

			String destPath = userDir + "/screenshots/" + DateUtil.getTimeStamp() + ".png";
			FileUtils.copyFile(sourceFile, new File(destPath));
			logger.addScreenCaptureFromPath(destPath);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/****************** Generate a JavascriptExecutor ******************/
	public JavascriptExecutor getJSExecutor() {
		return (JavascriptExecutor) driver;
	}

	/****************** Generate the body element ******************/
	public WebElement getPageBody() {
		return driver.findElement(By.tagName("body"));
	}

	/****************** To check if page loading is 'complete' ******************/
	public boolean isLoadComplete() {
		JavascriptExecutor je = getJSExecutor();
		return je.executeScript("return document.readyState;").toString().equals("complete");
	}

	/************** To check the freshness of a WebElement ******************/
	public boolean isFresh(WebElement element) {
		try {
			element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/****************** Scrolling methods ******************/
	public void scrollDown(int i) {
		getJSExecutor().executeScript("window.scrollBy(0," + (i * 100) + ");");
		waitFor(1);
	}

	public void scrollUp() {
		getPageBody().sendKeys(Keys.CONTROL, Keys.HOME);
	}

	/***************** Wait Functions in Framework *****************/
	public void waitForPageLoad() {
		try {
			driver.manage().timeouts().pageLoadTimeout(200, TimeUnit.SECONDS);
			while (!isLoadComplete()) {
				scrollUp();
			}
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	// User defined wait for each WebElement
	public void waitForElement(WebElement element) {
		while (!isFresh(element)) {
			continue;
		}
	}

	public void firefoxWait(double i) {
		if (driver instanceof FirefoxDriver) {
			waitFor(i);
		}
	}

	// User defined wait for each WebElement List
	public void waitForElements(List<WebElement> list) {
		for (WebElement element : list) {
			waitForElement(element);
		}
	}

	// Hardcoded wait
	public void waitFor(double i) {
		try {
			Thread.sleep((long) (i * 1000));
		} catch (InterruptedException e) {
			reportFail(e.getMessage());
		}
	}

}
